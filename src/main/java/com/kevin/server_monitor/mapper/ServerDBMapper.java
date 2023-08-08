package com.kevin.server_monitor.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ServerDBMapper {

    List<Map<String, Object>> detectServerList(Map<String, Object> map);
    List<Map<String, Object>> detectServerRowList(Map<String, Object> map);
    Map<String, Object> detectServer(Map<String, Object> map);
    int insertServerSensor(Map<String, Object> map);
    int updateServerSensor(Map<String, Object> map);
    int deleteServerSensor(Map<String, Object> map);
}
