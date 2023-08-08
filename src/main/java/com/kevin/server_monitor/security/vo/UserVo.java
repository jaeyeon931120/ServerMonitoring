package com.kevin.server_monitor.security.vo;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserVo {
    private String username;
    private String id;
    private String tel;
    private String password;
    private String author;
    private String memo;
}
