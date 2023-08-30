package com.kevin.server_monitor.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServerLogDto {
    /* 로그 테이블 컬럼 */
    private String system;
    private String ip;
    private String server_name;
    private String val_date;
    private String log;

    /* 로그 조회시 페이징을 위한 DTO */
    private SearchDto searchDto;

    /* LOG테이블 검색시 필요한 조건 */
    private String from_date;
    private String to_date;
    private String hours;
    private String process;
}
