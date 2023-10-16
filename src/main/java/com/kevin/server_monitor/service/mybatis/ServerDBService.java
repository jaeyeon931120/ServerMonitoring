package com.kevin.server_monitor.service.mybatis;

import com.kevin.server_monitor.mapper.ServerDBMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ServerDBService {
    protected final ServerDBMapper serverDBMapper;

    public ServerDBService(ServerDBMapper serverDBMapper) {
        this.serverDBMapper = serverDBMapper;
    }

    public List<Map<String, Object>> detectServerList(Map<String, Object> map) {
        return serverDBMapper.detectServerList(map);
    }

    public List<Map<String, Object>> detectServerRowList(Map<String, Object> map) {
        return serverDBMapper.detectServerRowList(map);
    }

    public List<Map<String, Object>> detectTrapicRowList(Map<String, Object> map) {
        return serverDBMapper.detectTrapicRowList(map);
    }

    public Map<String, Object> detectServer(Map<String, Object> map) {
        return serverDBMapper.detectServer(map);
    }

    public int insertServerSensor(Map<String, Object> map) {
        return serverDBMapper.insertServerSensor(map);
    }

    public int updateServerSensor(Map<String, Object> map) {
        return serverDBMapper.updateServerSensor(map);
    }

    public int deleteServerSensor(Map<String, Object> map) {
        return serverDBMapper.deleteServerSensor(map);
    }

    public void deleteServerLog() {
        serverDBMapper.deleteServerLog();
    }
    public void deleteServerRaw() {
        serverDBMapper.deleteServerRaw();
    }
}
