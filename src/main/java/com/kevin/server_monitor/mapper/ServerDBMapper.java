package com.kevin.server_monitor.mapper;

import com.kevin.server_monitor.dto.ServerLogDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ServerDBMapper {

    /**
     * 서버 정보 리스트 (센서 테이블)
     * @return 게시글 수
     */
    List<Map<String, Object>> detectServerList(Map<String, Object> map);

    /**
     * 트래픽 데이터 리스트 (raw 테이블)
     * @return 게시글 수
     */
    List<Map<String, Object>> detectTrapicRowList(Map<String, Object> map);

    /**
     * 서버 로그 리스트
     * @return 게시글 수
     */
    List<ServerLogDto> detectServerLogList(ServerLogDto serverLogDto);
    
    /**
     * 서버 리스트
     * @return 게시글 수
     */
    Map<String, Object> detectServer(Map<String, Object> map);

    /**
     * 서버 정보 INSERT
     * @return 게시글 수
     */
    int insertServerSensor(Map<String, Object> map);

    /**
     * 서버 정보 UPDATE
     * @return 게시글 수
     */
    int updateServerSensor(Map<String, Object> map);

    /**
     * 서버 정보 DELETE
     * @return 게시글 수
     */
    int deleteServerSensor(Map<String, Object> map);

    /**
     * 로그 수 카운팅
     * @return 게시글 수
     */
    int logCount(ServerLogDto params);

    /**
     * 로그 테이블 전체 삭제(하루 단위)
     */
    void deleteServerLog();

    /**
     * 서버 정보 RAW테이블 전체 삭제(하루 단위)
     */
    void deleteServerRaw();
}
