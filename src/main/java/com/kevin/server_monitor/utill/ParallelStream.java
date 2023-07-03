//package com.kevin.server_monitor.utill;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.kevin.server_monitor.common.GlobalProperties;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class ParallelStream {
//    private static final SSHUtils sshUtils = new SSHUtils();
//    private static final GlobalProperties gp = GlobalProperties.getInstance();
//    static Logger logger = LoggerFactory.getLogger(ParallelStream.class);
//
//    public static void serverParallel(String server) {
//        String result = "";
//        Map<String, Object> resultMap = new HashMap<>();
//
//        try {
//            logger.info("server : " + server);
//
//            String username = gp.getPropertyValue(server + "_username");
//            String password = gp.getPropertyValue(server + "_password");
//            String portString = gp.getPropertyValue(server + "_port");
//            String tomcatPortString = gp.getPropertyValue(server + "_tomcat_port");
//            String scriptdir = gp.getPropertyValue(server + "_dir");
//            String ip = gp.getPropertyValue(server + "_ip");
//            String system = gp.getPropertyValue(server + "_system");
//
//            logger.info("infomation : " + username + password + portString + tomcatPortString + scriptdir + ip);
//
//            if (!username.trim().equals("")) {
//                int port;
//                int tomcatport = 0;
//                if (!portString.trim().equals("")) {
//                    port = Integer.parseInt(portString);
//                } else {
//                    port = 0;
//                }
//                if (!tomcatPortString.trim().equals("")) {
//                    tomcatport = Integer.parseInt(tomcatPortString);
//                }
//
//                String command = "";
//
//                command = "cd " + scriptdir + " && ./server_monitoring " + ip + " " + tomcatport;
//                logger.info("command: " + command);
//                String finalCommand = command;
//                result = sshUtils.sshCommunication(username, password, ip, port, finalCommand);
//
//                if (!result.equals("")) {
//                    result = result;
//                } else {
//                    result = null;
//                }
//                logger.info("result : " + result);
//
//                ObjectMapper objectMapper = new ObjectMapper();
//                TypeReference<Map<String, Object>> typeReference = new TypeReference<Map<String, Object>>() {
//                };
//
//                resultMap = objectMapper.readValue(result, typeReference);
//
//                Double memorydouble = Double.parseDouble(resultMap.get("memory").toString());
//                Double diskdouble = Double.parseDouble(resultMap.get("disk").toString());
//                String memory = String.format("%.2f", memorydouble) + "%";
//                String disk = String.format("%.2f", diskdouble) + "%";
//                String cpu = resultMap.get("cpu").toString() + "%";
//
//                String portstatus = resultMap.get("port_status").toString();
//                if(portstatus.equals("0")){
//                    portstatus = "가동";
//                } else if(portstatus.equals("1")) {
//                    portstatus = "정지";
//                }
//
//                String rx = resultMap.get("RX").toString();
//                String tx = resultMap.get("TX").toString();
//                String rxsubstring = rx.substring(rx.length()-2);
//                String txsubstring = tx.substring(tx.length()-2);
//                logger.info("rxsubstring : " + rxsubstring);
//                logger.info("txsubstring : " + txsubstring);
//
//                if(!rxsubstring.equals("Kb")) {
//                    double rxdouble;
//                    rx = rx.substring(0, rx.length()-1);
//                    rxdouble = Math.round((Double.parseDouble(rx)/1000)*100)/100.0;
//                    rx = Double.toString(rxdouble) + "Kb";
//                }
//
//                if(!txsubstring.equals("Kb")) {
//                    double txdouble;
//                    tx = tx.substring(0, tx.length()-1);
//                    txdouble = Math.round((Double.parseDouble(tx)/1000)*100)/100.0;
//                    tx = Double.toString(txdouble) + "Kb";
//                }
//
//                String serverName = server;
//                String name_check = serverName.substring(serverName.length()-1);
//                if(isInteger(name_check)) {
//                    serverName = serverName.substring(0, serverName.length() - 1);
//                }
//
//                resultMap.put("RX", rx);
//                resultMap.put("TX", tx);
//                resultMap.put("cpu", cpu);
//                resultMap.put("memory", memory);
//                resultMap.put("disk", disk);
//                resultMap.put("port_status", portstatus);
//                resultMap.put("server_name", serverName);
//                resultMap.put("system", system);
//                resultMap.put("ip", ip);
//                resultMap.put("port", tomcatport);
//
//                logger.info("resultMap(result to Map) : " + resultMap);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static boolean isInteger(String strValue) {
//        try {
//            Integer.parseInt(strValue);
//            return true;
//        } catch (NumberFormatException ex) {
//            return false;
//        }
//    }
//}
