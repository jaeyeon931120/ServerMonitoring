package com.kevin.server_monitor.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface MonitorMapper {
    int getIDCheck(Map<String, Object> map); // 중복 아이디 확인
}
