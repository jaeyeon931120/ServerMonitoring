package com.kevin.server_monitor.controller;

import com.kevin.server_monitor.security.service.UserService;
import com.kevin.server_monitor.security.vo.UserVo;
import com.kevin.server_monitor.service.ServerInfoService;
import com.kevin.server_monitor.service.ServerManageService;
import com.kevin.server_monitor.service.mybatis.ServerDBService;
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
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class MonitorController {

    private static final SSHUtils sshUtils = new SSHUtils();
    private static final Logger logger = LoggerFactory.getLogger(MonitorController.class);
    private final UserService userService;
    private final ServerInfoService serverInfoService;
    private final ServerManageService serverManageService;
    private final ServerDBService serverDBService;

    public MonitorController(UserService userService, ServerInfoService serverInfoService,
                             ServerManageService serverManageService, ServerDBService serverDBService) {
        this.userService = userService;
        this.serverInfoService = serverInfoService;
        this.serverDBService = serverDBService;
        this.serverManageService = serverManageService;
    }

    @RequestMapping("/server_list")
    @ResponseBody
    public Map<String, Object> getServerlist(HttpServletRequest request) {
        List<Map<String, Object>> returnList;
        Map<String, Object> returnMap = new HashMap<>();

        try {
            HttpSession session = request.getSession();

            returnList = serverDBService.detectServerList(null);

            returnList.sort(
                    Comparator.comparing((Map<String, Object> map) -> (String)map.get("system"))
                            .thenComparing((Map<String, Object> map) -> (String)map.get("ip"))
                            .thenComparing((Map<String, Object> map) -> (String)map.get("server_name"))
                            .thenComparing((Map<String, Object> map) -> (Integer)map.get("tomcat_port"))
            );

            returnMap.put("author", session.getAttribute("author"));
            returnMap.put("server_list", returnList);
        } catch (Exception e) {
            logger.error("서버 정보 리스트를 불러오는 중에 오류가 발생했습니다.");
            e.printStackTrace();
        }

        return returnMap;
    }

    @RequestMapping("/monitoring")
    public ModelAndView getMonitoring(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        try {
            // token에 저장되어 있는 인증된 사용자의 id 값
            String id = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserVo userVo = userService.getUserById(id);
            String name = userVo.getUsername();
            String author = userVo.getAuthor();

            HttpSession session = request.getSession();
            session.setAttribute("id", id);
            session.setAttribute("username", name);
            session.setAttribute("author", author);
            // 세션 유지시간 설정(초단위) - 30분
            session.setMaxInactiveInterval(30*60);

            if(author.contains("ADMIN")) {
                view.addObject("id", id);
                view.addObject("username", name);
                view.addObject("author", author);
                view.setViewName("monitoring_admin");
            } else if(author.contains("USER")) {
                view.addObject("id", id);
                view.addObject("username", name);
                view.addObject("author", author);
                view.setViewName("monitoring_user");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    @RequestMapping("/power")
    @ResponseBody
    public Map<String, Object> getPower(@RequestBody Map<String, Object> req) {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> inputMap = new HashMap<>();
        String resultStr;
        String result;
        int updateresult;
        String power = null;

        try {
            power = req.get("power").toString();

            Map<String, Object> detectMap = new HashMap<>();
            String server_name = req.get("server_name").toString();
            String ip = req.get("ip").toString();
            int tomcat_port = Integer.parseInt(req.get("tomcat_port").toString());

            detectMap.put("server_name", server_name);
            detectMap.put("ip", ip);
            detectMap.put("tomcat_port", tomcat_port);

            Map<String, Object> serverMap = serverDBService.detectServer(detectMap);
            detectMap.remove("tomcat_port");

            String tomcat_dir = serverMap.get("tomcat_dir").toString();
            String id = serverMap.get("id").toString();
            String pw = serverMap.get("pw").toString();
            int server_port = Integer.parseInt(serverMap.get("server_port").toString());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            Calendar cal = Calendar.getInstance(Locale.KOREA);
            String val_date = sdf.format(cal.getTime());

            String command = "echo '" + pw + "' | sudo -S sh check_server_info/power_controller " + tomcat_dir;

            inputMap.put("ip", ip);
            inputMap.put("server_name", server_name);

            if (power.equals("on")) {
                command = command + " on";
                resultStr = sshUtils.sshControll(id, pw, ip, server_port, command);
                if (resultStr.contains("Tomcat started.")) {
                    result = "ok";
                    inputMap.put("status", "가동");
                    updateresult = serverDBService.updateServerSensor(inputMap);

                    if(updateresult < 0) {
                        logger.error("서버 가동상태를 DB에 업데이트 도중에 오류가 발생했습니다.");
                    }
                } else if (resultStr.contains("during start.") || resultStr.contains("Start aborted.")) {
                    result = "reason";
                } else {
                    result = "nok";
                }
            } else if (power.equals("off")) {
                command = command + " off";
                resultStr = sshUtils.sshControll(id, pw, ip, server_port, command);

                if (resultStr.contains("Tomcat stopped") || resultStr.contains("The Tomcat process has been killed")) {
                    inputMap.put("status", "정지");
                    inputMap.put("end_date", val_date);
                    result = "ok";
                    updateresult = serverDBService.updateServerSensor(inputMap);

                    if(updateresult < 0) {
                        logger.error("서버 가동상태를 DB에 업데이트 도중에 오류가 발생했습니다.");
                    }
                } else if (resultStr.contains("Stop aborted")) {
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
            logger.error("서버를 {}",power+" 하는중에 오류가 발생했습니다.");
            e.printStackTrace();
        }

        return resultMap;
    }

    @RequestMapping("/id_check")
    @ResponseBody
    public Map<String, Object> getIDCheck(@RequestBody Map<String, Object> req) {
        Map<String, Object> resultMap = new HashMap<>();

        try {
            String id = req.get("id").toString();
            int result;

            result = userService.getIDCheck(id);

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
            serverInfoService.serverInfo();
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

    @RequestMapping("/trapic_data")
    @ResponseBody
    public List<Map<String, Object>> getTrapicDatalist(@RequestBody Map<String, Object> req) {
        List<Map<String, Object>> returnList = new ArrayList<>();

        try {
            String val_date;
            String from_date;
            String to_date;
            req.remove("tomcat_port");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Calendar cal = Calendar.getInstance(Locale.KOREA);
            val_date = sdf.format(cal.getTime());
            from_date = val_date + "000000";
            to_date = val_date + "235959";
            req.put("from_date", from_date);
            req.put("to_date", to_date);

            returnList = serverDBService.detectTrapicRowList(req);
        } catch (Exception e) {
            logger.error("서버 정보 리스트를 불러오는 중에 오류가 발생했습니다.");
            e.printStackTrace();
        }
        return returnList;
    }

    @RequestMapping("/log_data")
    @ResponseBody
    public List<Map<String, Object>> getLogDatalist(@RequestBody Map<String, Object> req) {
        List<Map<String, Object>> returnList = new ArrayList<>();

        try {
            returnList = serverDBService.detectServerLogList(req);
        } catch (Exception e) {
            logger.error("서버 로그 리스트를 불러오는 중에 오류가 발생했습니다.");
            e.printStackTrace();
        }
        return returnList;
    }

    @RequestMapping("/alarm_data")
    @ResponseBody
    public List<Map<String, Object>> getAlarmDatalist(@RequestBody Map<String, Object> req) {
        List<Map<String, Object>> returnList = new ArrayList<>();

        try {
            String val_date;
            String from_date;
            String end_from_date;
            String to_date;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Calendar cal = Calendar.getInstance(Locale.KOREA);
            val_date = sdf.format(cal.getTime());
            to_date = val_date + "235959";

            cal.add(Calendar.DATE, -7); // 최근 일주일안의 종료된 시간만 검색
            from_date = val_date + "000000";
            end_from_date = val_date + "000000";

            req.put("end_date", end_from_date);
            req.put("from_date", from_date);
            req.put("to_date", to_date);

            returnList = serverDBService.detectServerList(req);
        } catch (Exception e) {
            logger.error("서버 로그 리스트를 불러오는 중에 오류가 발생했습니다.");
            e.printStackTrace();
        }
        return returnList;
    }
}
