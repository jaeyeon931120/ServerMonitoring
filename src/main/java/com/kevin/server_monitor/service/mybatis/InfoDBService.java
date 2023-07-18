package com.kevin.server_monitor.service.mybatis;

import com.kevin.server_monitor.mapper.ServerDBMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class InfoDBService {
    protected final ServerDBMapper serverDBMapper;

    public InfoDBService(ServerDBMapper serverDBMapper) {
        this.serverDBMapper = serverDBMapper;
    }

    public List<Map<String, Object>> selectServerInfo() {
        return serverDBMapper.selectServerInfo();
    }
}
