package com.kevin.server_monitor.service.server;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kevin.server_monitor.service.mybatis.BatchService;
import com.kevin.server_monitor.service.mybatis.ServerDBService;
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
    private final ServerDBService serverDBService;
    private final BatchService batchService;
    private List<Map<String, Object>> serverInfoList = new ArrayList<>();
    private List<Map<String, Object>> serverLogList = new ArrayList<>();

    public ServerInfoService(ServerDBService serverDBService, BatchService batchService) {
        this.serverDBService = serverDBService;
        this.batchService = batchService;
    }

    private void serverInformation(Map<String, Object> map) {
        String result;
        String val_date;
        Map<String, Object> resultMap;

        try {
            String server_name = map.get("server_name").toString();
            String user_id = map.get("id").toString();
            String user_pw = map.get("pw").toString();
            int serverport = Integer.parseInt(map.get("server_port").toString());
            int tomcatport = Integer.parseInt(map.get("tomcat_port").toString());
            String infodir = map.get("info_dir").toString();
            String ip = map.get("ip").toString();
            String system = map.get("system").toString();

            String command;

            command = "cd " + infodir + " && echo '" + user_pw + "' | sudo -S ./server_monitoring " + tomcatport;
            String finalCommand = command;
            result = sshUtils.sshControll(user_id, user_pw, ip, serverport, finalCommand);

            if (result.isEmpty()) {
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
            String end_date = null;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            Calendar cal = Calendar.getInstance(Locale.KOREA);
            val_date = sdf.format(cal.getTime());

            String portstatus = resultMap.get("port_status").toString();
            if (portstatus.equals("0")) {
                portstatus = "가동";
            } else if (portstatus.equals("1")) {
                end_date = val_date;
                portstatus = "정지";
            }

            String rx = resultMap.get("RX").toString();
            String tx = resultMap.get("TX").toString();
            rx = unitchage(rx);
            tx = unitchage(tx);

            resultMap = new HashMap<>();

            resultMap.put("trapic_rx", rx);
            resultMap.put("trapic_tx", tx);
            resultMap.put("cpu", cpu);
            resultMap.put("memory", memory);
            resultMap.put("disk", disk);
            resultMap.put("status", portstatus);
            resultMap.put("server_name", server_name);
            resultMap.put("system", system);
            resultMap.put("ip", ip);
            resultMap.put("tomcat_port", tomcatport);
            resultMap.put("val_date", val_date);
            resultMap.put("end_date", end_date);

            serverInfoList.add(resultMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void serverLog(Map<String, Object> map) {
        String result;
        Map<String, Object> resultMap;

        try {
            String server_name = map.get("server_name").toString();
            String user_id = map.get("id").toString();
            String user_pw = map.get("pw").toString();
            int serverport = Integer.parseInt(map.get("server_port").toString());
            int tomcatport = Integer.parseInt(map.get("tomcat_port").toString());
            String infodir = map.get("info_dir").toString();
            String ip = map.get("ip").toString();
            String system = map.get("system").toString();

            SimpleDateFormat sdf_month = new SimpleDateFormat("MMM", Locale.ENGLISH);
            SimpleDateFormat sdf_hour = new SimpleDateFormat("HH");
            SimpleDateFormat sdf_minute = new SimpleDateFormat("mm");
            Calendar cal = Calendar.getInstance(Locale.KOREA);
            int minute = cal.get(Calendar.MINUTE);

            if (minute == 0) {
                cal.add(Calendar.MINUTE, -1);
            }

            String val_hour = sdf_hour.format(cal.getTime());

            String val_minute = sdf_minute.format(cal.getTime());
            String first_minute = val_minute.substring(0, 1);
            int second_minute = Integer.parseInt(val_minute.substring(1, 2));
            if(second_minute == 0) {
                cal.add(Calendar.MINUTE, -1);

                val_hour = sdf_hour.format(cal.getTime());
                val_minute = sdf_minute.format(cal.getTime());
                first_minute = val_minute.substring(0, 1);
                second_minute = Integer.parseInt(val_minute.substring(1, 2));
            }
            int third_minute = second_minute - 1;

            String cmd_minute = first_minute + "["+ third_minute + "-" + second_minute + "]";

            String command;

            command = "cd " + infodir + " && echo '" + user_pw + "' | sudo -S ./log_monitoring " + val_hour + " " + cmd_minute;

            String finalCommand = command;
            result = sshUtils.sshControll(user_id, user_pw, ip, serverport, finalCommand);

            String[] returnlist = result.split("\n");

            for(String all_log : returnlist) {
                String[] log_units = all_log.split(" ");

                String val_date = log_units[2];
                if(!val_date.contains(":")) {
                    val_date = log_units[3];
                }
                StringBuilder end_log = new StringBuilder();

                for(int i = 4; i < log_units.length; i++) {
                    if (i != log_units.length - 1) {
                        end_log.append(log_units[i]).append(" ");
                    } else {
                        end_log.append(log_units[i]);
                    }
                }

                String log = end_log.toString();

                resultMap = new HashMap<>();

                resultMap.put("server_name", server_name);
                resultMap.put("system", system);
                resultMap.put("ip", ip);
                resultMap.put("tomcat_port", tomcatport);
                resultMap.put("val_date", val_date);
                resultMap.put("log", log);

                serverLogList.add(resultMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveServerInfo() {
        try {
            List<Map<String, Object>> dataList;
            List<Map<String, Object>> logList;
            dataList = serverInfoList;

            if(!serverInfoList.isEmpty()) {
                batchService.sqlSessionBatch(dataList, "insert", "data");
                batchService.sqlSessionBatch(dataList, "update", "data");
                serverInfoList = new ArrayList<>();
            } else {
                logger.error("서버와 수신중에 오류가 발생하여 데이터가 없어서 DB적제에 실패했습니다.");
            }

            logList = serverLogList;

            if(!serverLogList.isEmpty()) {
                batchService.sqlSessionBatch(logList, "insert", "log");
                serverLogList = new ArrayList<>();
            } else {
                logger.error("서버와 수신중에 오류가 발생하여 기록할 로그가 없어서 DB적제에 실패했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String unitchage(String trafic) {
        if (trafic.contains("Kb")) {
            return trafic;
        } else if (trafic.contains("Mb")) {
            double txdouble;
            trafic = trafic.substring(0, trafic.length() - 2);
            txdouble = Math.round(Double.parseDouble(trafic) * 1024);
            trafic = txdouble + "Kb";
        } else {
            double txdouble;
            trafic = trafic.substring(0, trafic.length() - 1);
            txdouble = Math.round((Double.parseDouble(trafic) / 1024) * 100) / 100.0;
            trafic = txdouble + "Kb";
        }
        return trafic;
    }

    public void serverInfo() {
        List<Map<String, Object>> serverlist;

        try {
            serverlist = serverDBService.detectServerList(null);

            Stream<Map<String, Object>> parallelStream_information = serverlist.parallelStream();
            parallelStream_information.forEach(this::serverInformation);
            logger.info("Server information DATA");
            Stream<Map<String, Object>> parallelStream_log = serverlist.parallelStream();
            parallelStream_log.forEach(this::serverLog);
            logger.info("Server information LOG");
            saveServerInfo();
            logger.info("Server information INSERT");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void partServerInfo(Map<String, Object> map) {
        try {
            serverInformation(map);
            serverLog(map);
            saveServerInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteServerLog() {
        try {
            serverDBService.deleteServerLog();
        } catch (Exception e) {
            logger.error("서버 LOG테이블 삭제중에 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }

    public void deleteServerRaw() {
        try {
            serverDBService.deleteServerRaw();
        } catch (Exception e) {
            logger.error("서버 RAW테이블 삭제중에 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }
}
