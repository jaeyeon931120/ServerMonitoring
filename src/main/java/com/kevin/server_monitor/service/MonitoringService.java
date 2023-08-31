package com.kevin.server_monitor.service;

import com.kevin.server_monitor.common.paging.PagingResponse;
import com.kevin.server_monitor.dto.SearchDto;
import com.kevin.server_monitor.dto.ServerLogDto;
import com.kevin.server_monitor.security.service.UserService;
import com.kevin.server_monitor.security.vo.UserVo;
import com.kevin.server_monitor.service.mybatis.ServerDBService;
import com.kevin.server_monitor.service.server.ServerInfoService;
import com.kevin.server_monitor.service.server.ServerManageService;
import com.kevin.server_monitor.util.SSHUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MonitoringService {
    private static final SSHUtils sshUtils = new SSHUtils();
    private static final Logger logger = LoggerFactory.getLogger(MonitoringService.class);
    private final UserService userService;
    private final ServerInfoService serverInfoService;
    private final ServerManageService serverManageService;
    private final ServerDBService serverDBService;
    private final PagingService pagingService;

    public MonitoringService(PagingService pagingService, UserService userService,
                             ServerInfoService serverInfoService, ServerManageService serverManageService,
                             ServerDBService serverDBService) {
        this.pagingService = pagingService;
        this.userService = userService;
        this.serverInfoService = serverInfoService;
        this.serverDBService = serverDBService;
        this.serverManageService = serverManageService;
    }

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

    public ModelAndView getMonitoring(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        try {
            // token에 저장되어 있는 인증된 사용자의 id 값
            String id = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(id != null) {
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
            } else {
                view.setViewName("login");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    public String getSessionCheck() {
        String id = null;
        try {
            id = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return id;
    }

    public Map<String, Object> getPower(Map<String, Object> req) {
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

    public Map<String, Object> getIDCheck(Map<String, Object> req) {
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

    public Map<String, Object> getServerPlus(Map<String, Object> req) {
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

    public Map<String, Object> getServerDelete(Map<String, Object> req) {
        Map<String, Object> resultMap = new HashMap<>();

        try {
            resultMap = serverManageService.serverDeleteService(req);
        } catch (Exception e) {
            resultMap.put("result", "nok");
            e.printStackTrace();
        }

        return resultMap;
    }

    public List<Map<String, Object>> getTrapicDatalist(Map<String, Object> req) {
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

    public PagingResponse<ServerLogDto> getLogDatalist(ServerLogDto serverLogDto) {
        PagingResponse<ServerLogDto> returnList = null;

        try {
            String hours = serverLogDto.getHours();
            String process = serverLogDto.getProcess();
            String from_date;
            String to_date;

            if(hours.equals("00")) {
                from_date = "00:00:00";
                to_date = "00:59:59";
            } else {
                from_date = hours + ":00:00";
                to_date = hours + ":59:59";
            }

            if(process.equals("main")) {
                serverLogDto.setFrom_date(from_date);
            } else if(process.equals("popup")) {
                serverLogDto.setFrom_date(from_date);
                serverLogDto.setTo_date(to_date);
            }

            if (serverLogDto.getSearchDto() == null) {
                serverLogDto.setSearchDto(new SearchDto());
            }
            returnList = pagingService.findAllLog(serverLogDto);
        } catch (Exception e) {
            logger.error("서버 로그 리스트를 불러오는 중에 오류가 발생했습니다.");
            e.printStackTrace();
        }
        return returnList;
    }

    public List<Map<String, Object>> getAlarmDatalist(Map<String, Object> req) {
        List<Map<String, Object>> returnList = new ArrayList<>();

        try {
            String val_date;
            String from_date;
            String to_date;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Calendar cal = Calendar.getInstance(Locale.KOREA);
            val_date = sdf.format(cal.getTime());
            to_date = val_date + "235959";

            cal.add(Calendar.DATE, -7); // 최근 일주일안의 종료된 시간만 검색
            val_date = sdf.format(cal.getTime());
            from_date = val_date + "000000";

            req.put("from_date", from_date);
            req.put("to_date", to_date);
            req.put("end_date", to_date);

            returnList = serverDBService.detectServerList(req);
        } catch (Exception e) {
            logger.error("서버 로그 리스트를 불러오는 중에 오류가 발생했습니다.");
            e.printStackTrace();
        }
        return returnList;
    }
}
