package com.kevin.server_monitor.service;

import com.kevin.server_monitor.service.mybatis.ServerDBService;
import com.kevin.server_monitor.util.SSHUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class ServerManageService {

    private final Logger logger = LoggerFactory.getLogger(ServerManageService.class);
    private final SSHUtils sshUtils = new SSHUtils();

    private final ServerDBService serverDBService;
    private final ServerInfoService serverInfoService;

    public ServerManageService(ServerDBService serverDBService, ServerInfoService serverInfoService) {
        this.serverDBService = serverDBService;
        this.serverInfoService = serverInfoService;
    }

    public Map<String, Object> serverInsertService(Map<String, Object> map) {
        int result;
        Map<String, Object> insertMap = new HashMap<>();
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> detectMap;
        logger.info("ServerInsertService start!");

        try {
            int serverport = Integer.parseInt(map.get("server_port").toString());
            int tomcatport = Integer.parseInt(map.get("tomcat_port").toString());
            String server_name = map.get("server_name").toString();
            String id = map.get("id").toString();
            String pw = map.get("pw").toString();
            String info_dir = "/home/dev/check_server_info";
            String tomcat_dir = map.get("tomcat_dir").toString();
            String ip = map.get("ip").toString();
            String system = map.get("system").toString();

            insertMap.put("system", system);
            insertMap.put("ip", ip);
            insertMap.put("server_name", server_name);
            insertMap.put("tomcat_port", tomcatport);
            insertMap.put("id", id);
            insertMap.put("pw", pw);
            insertMap.put("server_port", serverport);
            insertMap.put("info_dir", info_dir);
            insertMap.put("tomcat_dir", tomcat_dir);
            insertMap.put("key", salt());

            String fileName_info = URLEncoder.encode("server_monitoring", StandardCharsets.UTF_8);
            String fileName_power = URLEncoder.encode("power_controller", StandardCharsets.UTF_8);
            sshUtils.sftpCommunication(id, pw, ip, serverport, "insert", fileName_info);
            sshUtils.sftpCommunication(id, pw, ip, serverport, "insert", fileName_power);
            result = serverDBService.insertServerSensor(insertMap);
            if(result <= 0) {
                detectMap = serverDBService.detectServer(insertMap);
                if(!detectMap.isEmpty()) {
                    logger.error("중복되는 서버가 존재합니다. 서버 리스트를 확인해주세요.");
                    resultMap.put("result", "detect");
                } else {
                    logger.error("서버 등록중에 오류가 발생했습니다.");
                    resultMap.put("result", "nok");
                }
            } else {
                resultMap.put("result", "ok");
            }

            serverInfoService.partServerInfo(insertMap);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("서버 등록중에 오류가 발생했습니다.");
            resultMap.put("result", "nok");
        }

        return resultMap;
    }

    public Map<String, Object> serverDeleteService(Map<String, Object> map) {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> deleteMap = new HashMap<>();
        Map<String, Object> detectMap;

        String system = map.get("system").toString();
        String ip = map.get("ip").toString();
        String id = map.get("id").toString();
        String pw = map.get("pw").toString();
        int serverport = Integer.parseInt(map.get("server_port").toString());
        int tomcatport = Integer.parseInt(map.get("tomcat_port").toString());
        String server_name = map.get("server_name").toString();

        deleteMap.put("system", system);
        deleteMap.put("ip", ip);
        deleteMap.put("id", id);
        deleteMap.put("pw", pw);
        deleteMap.put("server_name", server_name);
        deleteMap.put("tomcat_port", tomcatport);
        deleteMap.put("server_port", serverport);

        int result = serverDBService.deleteServerSensor(deleteMap);
        if(result > 0) {
            resultMap.put("result", "ok");
        } else {
            resultMap.put("result", "nok");
        }

        deleteMap.remove("tomcat_port");
        detectMap = serverDBService.detectServer(deleteMap);

        if(detectMap != null && detectMap.isEmpty()) {
            String fileName_info = URLEncoder.encode("server_monitoring", StandardCharsets.UTF_8);
            String fileName_power = URLEncoder.encode("power_controller", StandardCharsets.UTF_8);
            sshUtils.sftpCommunication(id, pw, ip, serverport, "delete", fileName_info);
            sshUtils.sftpCommunication(id, pw, ip, serverport, "delete", fileName_power);
        }

        return resultMap;
    }

    public String salt() {

        String salt="";
        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            byte[] bytes = new byte[16];
            random.nextBytes(bytes);
            salt = new String(Base64.getEncoder().encode(bytes));

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return salt;
    }
}
