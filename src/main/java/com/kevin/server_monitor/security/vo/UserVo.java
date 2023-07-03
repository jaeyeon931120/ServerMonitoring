package com.kevin.server_monitor.security.vo;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserVo {
    private Long pk;
    private String name;
    private String id;
    private String email;
    private String password;
}
