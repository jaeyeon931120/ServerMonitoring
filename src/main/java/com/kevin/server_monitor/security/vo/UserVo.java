package com.kevin.server_monitor.security.vo;


import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserVo {
    private String id;
    private String username;
    private String tel;
    private String password;
    private String author;
    private String memo;
}
