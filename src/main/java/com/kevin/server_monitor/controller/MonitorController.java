package com.kevin.server_monitor.controller;

import com.kevin.server_monitor.common.paging.PagingResponse;
import com.kevin.server_monitor.dto.ServerLogDto;
import com.kevin.server_monitor.service.MonitoringService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MonitorController {

    private static final Logger logger = LoggerFactory.getLogger(MonitorController.class);
    private final MonitoringService monitoringService;

    public MonitorController(MonitoringService monitoringService) {
        this.monitoringService = monitoringService;
    }

    @PostMapping("/server_list")
    @ResponseBody
    public Map<String, Object> getServerlist(HttpServletRequest request) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            returnMap = monitoringService.getServerlist(request);
        } catch (Exception e) {
            logger.error("An error occurred while retrieving the server information list. error message : {}", e.getMessage());
            e.printStackTrace();
        }

        return returnMap;
    }

    @RequestMapping("/monitoring")
    public ModelAndView getMonitoring(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        try {
            view = monitoringService.getMonitoring(request);
        } catch (Exception e) {
            logger.error("An error occurred while loading the server monitoring page. error message : {}", e.getMessage());
            e.printStackTrace();
        }

        return view;
    }

    @PostMapping("/power")
    @ResponseBody
    public Map<String, Object> getPower(@RequestBody Map<String, Object> req) {
        Map<String, Object> resultMap = new HashMap<>();

        try {
            resultMap = monitoringService.getPower(req);
        } catch (Exception e) {
            logger.error("An error occurred while control the server. error message : {}", e.getMessage());
            e.printStackTrace();
        }

        return resultMap;
    }

    @PostMapping("/id_check")
    @ResponseBody
    public Map<String, Object> getIDCheck(@RequestBody Map<String, Object> req) {
        Map<String, Object> resultMap = new HashMap<>();

        try {
            resultMap = monitoringService.getIDCheck(req);
        } catch (Exception e) {
            logger.error("An error occurred during ID duplication check. error message : {}", e.getMessage());
            e.printStackTrace();
        }

        return resultMap;
    }

    @PostMapping("/server_plus")
    @ResponseBody
    public Map<String, Object> getServerPlus(@RequestBody Map<String, Object> req) {
        Map<String, Object> resultMap = new HashMap<>();

        try {
            resultMap = monitoringService.getServerPlus(req);
        } catch (Exception e) {
            logger.error("An error occurred while adding a server. error message : {}", e.getMessage());
            resultMap.put("result", "nok");
            e.printStackTrace();
        }

        return resultMap;
    }

    @PostMapping("/server_delete")
    @ResponseBody
    public Map<String, Object> getServerDelete(@RequestBody Map<String, Object> req) {
        Map<String, Object> resultMap = new HashMap<>();

        try {
            resultMap = monitoringService.getServerDelete(req);
        } catch (Exception e) {
            logger.error("An error occurred while deleting a server. error message : {}", e.getMessage());
            resultMap.put("result", "nok");
            e.printStackTrace();
        }

        return resultMap;
    }

    @PostMapping("/trapic_data")
    @ResponseBody
    public List<Map<String, Object>> getTrapicDatalist(@RequestBody Map<String, Object> req) {
        List<Map<String, Object>> returnList = new ArrayList<>();

        try {
            returnList = monitoringService.getTrapicDatalist(req);
        } catch (Exception e) {
            logger.error("An error occurred while retrieving the server information list. error message : {}", e.getMessage(), e);
            e.printStackTrace();
        }
        return returnList;
    }

    @PostMapping("/log_data")
    @ResponseBody
    public PagingResponse<ServerLogDto> getLogDatalist(@RequestBody ServerLogDto serverLogDto) {
        PagingResponse<ServerLogDto> returnList = null;

        try {
            returnList = monitoringService.getLogDatalist(serverLogDto);
        } catch (Exception e) {
            logger.error("An error occurred while loading the server log list. error message : {}", e.getMessage());
            e.printStackTrace();
        }
        return returnList;
    }

    @PostMapping("/alarm_data")
    @ResponseBody
    public List<Map<String, Object>> getAlarmDatalist(@RequestBody Map<String, Object> req) {
        List<Map<String, Object>> returnList = new ArrayList<>();

        try {
            returnList = monitoringService.getAlarmDatalist(req);
        } catch (Exception e) {
            logger.error("An error occurred while loading the server alarm list. error message : {}", e.getMessage());
            e.printStackTrace();
        }
        return returnList;
    }
}
