package com.kevin.server_monitor.controller;

import com.kevin.server_monitor.common.GlobalProperties;
import com.kevin.server_monitor.security.service.UserService;
import com.kevin.server_monitor.security.vo.UserVo;
import com.kevin.server_monitor.service.ServerManageService;
import com.kevin.server_monitor.service.mybatis.MonitorService;
import com.kevin.server_monitor.util.RankingUtils;
import com.kevin.server_monitor.util.SSHUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
public class MonitorController {

    private static final SSHUtils sshUtils = new SSHUtils();
    private static final RankingUtils rankingUtils = new RankingUtils();
    private static final GlobalProperties gp = GlobalProperties.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(MonitorController.class);
    private final UserService userService;
    private final MonitorService monitorService;
    private final ServerManageService serverManageService;

    public MonitorController(UserService userService, MonitorService monitorService, ServerManageService serverManageService) {
        this.userService = userService;
        this.monitorService = monitorService;
        this.serverManageService = serverManageService;
    }

//    @RequestMapping("/server_list")
//    @ResponseBody
//    public static Map<String, Object> getServerlist() {
//        long beforeTime = System.currentTimeMillis();
//        Map<String, String> propertiesMap;
//        Map<String, Object> resultMap = new HashMap<>();
//        List<String> serverlist;
//        returnList = new ArrayList<>();
//
//        try {
//            propertiesMap = gp.getPropertyInformation(ServerString.GLOBAL_CONFIG_PROPERTIES);
//
//            serverlist = Arrays.asList(propertiesMap.get("server_list").split(","));
//
//            boolean duplicated = (serverlist.stream().distinct().count() != serverlist.size());
//
//            if(duplicated) {
//                StringBuilder server;
//                String DuplicateServer = "";
//                int count = 2;
//                for(int i = 0; i < serverlist.size(); i++) {
//                    server = new StringBuilder(serverlist.get(i));
//                    if(DuplicateServer.contentEquals(server)) {
//                        count++;
//                    } else {
//                        count = 2;
//                    }
//                    for(int j = 0; j < i; j++) {
//                        if(serverlist.get(i).equals(serverlist.get(j))) {
//                            DuplicateServer = serverlist.get(i);
//                            server.append(count);
//                        }
//                    }
//                    serverlist.set(i, server.toString());
//                }
//            }
//
//            Stream<String> parallelStream = serverlist.parallelStream();
//            parallelStream.forEach(MonitorController::serverinformation);
//            returnList = rankingUtils.getRankValues(returnList);
//            logger.info("returnList : " + returnList);
//            returnList.sort(
//                    Comparator.comparing((Map<String, Object> map) -> (String)map.get("system"))
//                            .thenComparing((Map<String, Object> map) -> (String)map.get("ip"))
//                            .thenComparing((Map<String, Object> map) -> (String)map.get("server_name"))
//                            .thenComparing((Map<String, Object> map) -> (Integer)map.get("port"))
//            );
//            resultMap.put("table", returnList);
//            int i = 0;
//            int j = 0;
//            List<Map<String, Object>> returnlist1 = new ArrayList<>();
//            List<Map<String, Object>> returnlist2 = new ArrayList<>();
//            List<Map<String, Object>> returnlist3 = new ArrayList<>();
//            returnlist1.add(returnList.get(j));
//            returnlist2.add(returnList.get(j));
//            returnlist3.add(returnList.get(j));
//            for (j = 1; j < returnList.size(); j++) {
//                if(!(returnList.get(j).get("server_name").equals(returnList.get(i).get("server_name")))) {
//                    returnlist1.add(returnList.get(j));
//                    returnlist2.add(returnList.get(j));
//                    returnlist3.add(returnList.get(j));
//                }
//                i=j;
//            }
//            j = i-1;
//            if(!(returnList.get(j).get("server_name").equals(returnList.get(i).get("server_name")))) {
//                returnlist1.add(returnList.get(j));
//                returnlist2.add(returnList.get(j));
//                returnlist3.add(returnList.get(j));
//            }
//            returnlist1.sort(
//                    Comparator.comparing((Map<String, Object> map) -> (Integer)map.get("cpu_rank"))
//            );
//            resultMap.put("ranking_cpu", returnlist1);
//            returnlist2.sort(
//                    Comparator.comparing((Map<String, Object> map) -> (Integer)map.get("memory_rank"))
//            );
//            resultMap.put("ranking_memory", returnlist2);
//            returnlist3.sort(
//                    Comparator.comparing((Map<String, Object> map) -> (Integer)map.get("disk_rank"))
//            );
//            resultMap.put("ranking_disk", returnlist3);
//            long afterTime = System.currentTimeMillis();
//            long secDiffTime = (afterTime - beforeTime)/1000;
//
//            logger.info("서버정보 탐색에 걸리는 시간(초) : " + secDiffTime);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return resultMap;
//    }

    @RequestMapping("/monitoring")
    public ModelAndView getMonitoring(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        try {
            // token에 저장되어 있는 인증된 사용자의 id 값
            String id = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserVo userVo = userService.getUserById(id);
            String name = userVo.getName();

            HttpSession session = request.getSession();
            session.setAttribute("id", id);
            session.setAttribute("username", name);

            logger.info("Server_Monitoring Start!");

            view.addObject("id", id);
            view.addObject("username", name);
            view.setViewName("/monitoring");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    @RequestMapping("/power")
    @ResponseBody
    public Map<String, Object> getPower(@RequestBody Map<String, Object> req) {
        Map<String, Object> resultMap = new HashMap<>();
        String resultStr;
        String result;
        String servername;
        String power;

        try {
            servername = req.get("server_name").toString();

            String username = gp.getPropertyValue(servername + "_username");
            String password = gp.getPropertyValue(servername + "_password");
            String portString = gp.getPropertyValue(servername + "_port");
            String scriptdir = gp.getPropertyValue(servername + "_start_dir");
            String ip = gp.getPropertyValue(servername + "_ip");

            int port = 0;
            if (!portString.equals("")) {
                port = Integer.parseInt(portString);
            }
            power = req.get("power").toString();
            String command;

            if (power.equals("on")) {
                command = "cd " + scriptdir + " && ./start.sh";
                resultStr = sshUtils.sshCommunication(username, password, ip, port, command);

                if (resultStr.contains("Tomcat started.")) {
                    result = "ok";
                } else if (resultStr.contains("during start.")) {
                    result = "reason";
                } else {
                    result = "nok";
                }
            } else if (power.equals("off")) {
                command = "cd " + scriptdir + " && ./stop.sh";
                resultStr = sshUtils.sshCommunication(username, password, ip, port, command);

                if (resultStr.contains("Tomcat stopped.")) {
                    result = "ok";
                } else if (resultStr.contains("Stop aborted.")) {
                    result = "reason";
                } else {
                    result = "nok";
                }
            } else {
                result = "nok";
                resultMap.put("result", result);

                return resultMap;
            }

            resultMap.put("result", result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultMap;
    }

    @RequestMapping("/id_check")
    @ResponseBody
    public Map<String, Object> getIDCheck(@RequestBody Map<String, Object> req) {
        Map<String, Object> dataMap = new HashMap<>();
        Map<String, Object> resultMap = new HashMap<>();

        try {
            String id = req.get("id").toString();
            dataMap.put("id", id);
            int result;

            result = monitorService.getIDCheck(dataMap);

            if(result > 0) {
                resultMap.put("result", "nok");
            } else {
                resultMap.put("result", "ok");
            }
        } catch (Exception e) {
            resultMap.put("result", "error");
            e.printStackTrace();
        }

        return resultMap;
    }

    @RequestMapping("/server_plus")
    @ResponseBody
    public Map<String, Object> getServerPlus(@RequestBody Map<String, Object> req) {
        Map<String, Object> resultMap = new HashMap<>();

        try {
            resultMap = serverManageService.serverInsertService(req);
        } catch (Exception e) {
            resultMap.put("result", "nok");
            e.printStackTrace();
        }

        return resultMap;
    }

    @RequestMapping("/server_delete")
    @ResponseBody
    public Map<String, Object> getServerDelete(@RequestBody Map<String, Object> req) {
        Map<String, Object> resultMap = new HashMap<>();

        try {
            resultMap = serverManageService.serverDeleteService(req);
        } catch (Exception e) {
            resultMap.put("result", "nok");
            e.printStackTrace();
        }

        return resultMap;
    }
}
