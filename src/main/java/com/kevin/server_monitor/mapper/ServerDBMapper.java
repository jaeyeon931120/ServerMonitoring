package com.kevin.server_monitor.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ServerDBMapper {

    List<Map<String, Object>> selectServerInfo();
    int insertServerSensor(Map<String, Object> map);
    Map<String, Object> detectServerInsert(Map<String, Object> map);
    int deleteServerSensor(Map<String, Object> map);
    Map<String, Object> detectServerDelete(Map<String, Object> map);
}
