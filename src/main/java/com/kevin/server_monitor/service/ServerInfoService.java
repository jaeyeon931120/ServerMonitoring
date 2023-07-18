package com.kevin.server_monitor.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kevin.server_monitor.service.mybatis.InfoDBService;
import com.kevin.server_monitor.util.SSHUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

@Service
public class ServerInfoService {

    private final Logger logger = LoggerFactory.getLogger(ServerInfoService.class);
    private final SSHUtils sshUtils = new SSHUtils();
    private final InfoDBService infoDBService;
    private final BatchService batchService;
    private List<Map<String, Object>> serverInfoList = new ArrayList<>();

    public ServerInfoService(InfoDBService infoDBService, BatchService batchService) {
        this.infoDBService = infoDBService;
        this.batchService = batchService;
    }

    private void serverinformation(Map<String, Object> map) {
        String result;
        String val_date;
        Map<String, Object> resultMap;

        try {
            String server_name = map.get("name").toString();
            String user_id = map.get("id").toString();
            String user_pw = map.get("pw").toString();
            int serverport = Integer.parseInt(map.get("server_port").toString());
            int tomcatport = Integer.parseInt(map.get("tomcat_port").toString());
            String infodir = map.get("info_dir").toString();
            String ip = map.get("ip").toString();
            String system = map.get("system").toString();

            String command;

            command = "cd " + infodir + " && ./server_monitoring " + ip + " " + tomcatport;
            String finalCommand = command;
            result = sshUtils.sshCommunication(user_id, user_pw, ip, serverport, finalCommand);

            if (result.equals("")) {
                result = null;
            }

            ObjectMapper objectMapper = new ObjectMapper();
            TypeReference<Map<String, Object>> typeReference = new TypeReference<>() {
            };

            resultMap = objectMapper.readValue(result, typeReference);

            Double memorydouble = Double.parseDouble(resultMap.get("memory").toString());
            Double diskdouble = Double.parseDouble(resultMap.get("disk").toString());
            String memory = String.format("%.2f", memorydouble) + "%";
            String disk = String.format("%.2f", diskdouble) + "%";
            String cpu = resultMap.get("cpu").toString() + "%";

            String portstatus = resultMap.get("port_status").toString();
            if (portstatus.equals("0")) {
                portstatus = "가동";
            } else if (portstatus.equals("1")) {
                portstatus = "정지";
            }

            String rx = resultMap.get("RX").toString();
            String tx = resultMap.get("TX").toString();
            String rxsubstring = rx.substring(rx.length() - 2);
            String txsubstring = tx.substring(tx.length() - 2);

            rx = unitchage(rx, rxsubstring);
            tx = unitchage(tx, txsubstring);

            String serverName = server_name;
            String name_check = serverName.substring(serverName.length() - 1);
            if (isInteger(name_check)) {
                serverName = serverName.substring(0, serverName.length() - 1);
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            Calendar cal = Calendar.getInstance(Locale.KOREA);
            val_date = sdf.format(cal.getTime());

            resultMap = new HashMap<>();

            resultMap.put("trapic_rx", rx);
            resultMap.put("trapic_tx", tx);
            resultMap.put("cpu", cpu);
            resultMap.put("memory", memory);
            resultMap.put("disk", disk);
            resultMap.put("status", portstatus);
            resultMap.put("name", serverName);
            resultMap.put("system", system);
            resultMap.put("ip", ip);
            resultMap.put("tomcat_port", tomcatport);
            resultMap.put("val_date", val_date);

            serverInfoList.add(resultMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveServerInfo() {
        try {
            List<Map<String, Object>> datalist;
            datalist = serverInfoList;

            if(!serverInfoList.isEmpty()) {
                batchService.sqlSessionBatch(datalist, "insert");
                batchService.sqlSessionBatch(datalist, "update");
            } else {
                logger.error("서버와 수신중에 오류가 발생하여 데이터가 없어서 DB적제에 실패했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String unitchage(String trafic, String traficstring) {
        if (!traficstring.equals("Kb")) {
            double txdouble;
            trafic = trafic.substring(0, trafic.length() - 1);
            txdouble = Math.round((Double.parseDouble(trafic) / 1000) * 100) / 100.0;
            trafic = txdouble + "Kb";
        }
        return trafic;
    }

    public void selectServerInfo() {
        List<Map<String, Object>> serverlist;
        serverInfoList = new ArrayList<>();

        try {
            serverlist = infoDBService.selectServerInfo();

            Stream<Map<String, Object>> parallelStream = serverlist.parallelStream();
            parallelStream.forEach(this::serverinformation);
            saveServerInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean isInteger(String strValue) {
        try {
            Integer.parseInt(strValue);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
}
