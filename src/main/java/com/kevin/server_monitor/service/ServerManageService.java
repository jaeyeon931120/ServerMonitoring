package com.kevin.server_monitor.service;

import com.kevin.server_monitor.mapper.ServerDBMapper;
import com.kevin.server_monitor.util.SSHUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ServerManageService {

    private final Logger logger = LoggerFactory.getLogger(ServerManageService.class);
    private final SSHUtils sshUtils = new SSHUtils();

    private final ServerDBMapper serverDBMapper;

    public ServerManageService(ServerDBMapper serverDBMapper) {
        this.serverDBMapper = serverDBMapper;
    }

    public Map<String, Object> serverInsertService(Map<String, Object> map) {
        int result;
        Map<String, Object> insertMap = new HashMap<>();
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> detectMap;
        logger.info("ServerInsertService start!");

        try {
            int serverport = Integer.parseInt(map.get("serverport").toString());
            int tomcatport = Integer.parseInt(map.get("tomcatport").toString());
            String server_name = map.get("name").toString();
            String id = map.get("id").toString();
            String pw = map.get("pw").toString();
            String info_dir = "/home/check_server_info";
            String tomcat_dir = map.get("tomcatdir").toString();
            String ip = map.get("ip").toString();
            String system = map.get("system").toString();

            insertMap.put("system", system);
            insertMap.put("ip", ip);
            insertMap.put("name", server_name);
            insertMap.put("tomcat_port", tomcatport);
            insertMap.put("id", id);
            insertMap.put("pw", pw);
            insertMap.put("server_port", serverport);
            insertMap.put("info_dir", info_dir);
            insertMap.put("tomcat_dir", tomcat_dir);

            sshUtils.sftpCommunication(id, pw, ip, serverport, "insert");
            result = serverDBMapper.insertServerSensor(insertMap);
            if(result <= 0) {
                detectMap = serverDBMapper.detectServerInsert(insertMap);
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
        int serverport = Integer.parseInt(map.get("serverport").toString());
        int tomcatport = Integer.parseInt(map.get("tomcatport").toString());
        String server_name = map.get("name").toString();

        deleteMap.put("system", system);
        deleteMap.put("ip", ip);
        deleteMap.put("id", id);
        deleteMap.put("pw", pw);
        deleteMap.put("name", server_name);
        deleteMap.put("tomcat_port", tomcatport);
        deleteMap.put("server_port", serverport);

        int result = serverDBMapper.deleteServerSensor(deleteMap);
        if(result > 0) {
            resultMap.put("result", "ok");
        } else {
            resultMap.put("result", "nok");
        }

        detectMap = serverDBMapper.detectServerDelete(deleteMap);

        if(detectMap.isEmpty()) {
           sshUtils.sftpCommunication(id, pw, ip, serverport, "delete");
        }

        return resultMap;
    }
}
