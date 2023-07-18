package com.kevin.server_monitor.service.mybatis;

import com.kevin.server_monitor.mapper.MonitorMapper;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MonitorService {
    private final MonitorMapper monitorMapper;

    public MonitorService(MonitorMapper monitorMapper) {
        this.monitorMapper = monitorMapper;
    }

    public int getIDCheck(Map<String, Object> map) {
        return monitorMapper.getIDCheck(map);
    }
}
