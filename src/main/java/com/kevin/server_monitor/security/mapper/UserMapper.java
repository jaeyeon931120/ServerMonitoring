package com.kevin.server_monitor.security.mapper;

import com.kevin.server_monitor.security.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    List<UserVo> getUserList(); // User 테이블 가져오기
    void insertUser(UserVo userVo); // 회원 가입
    UserVo getUserByPk(Long pk);
    UserVo getUserById(String id);
    void updateUser(UserVo userVo); // 회원 정보 수정
    void deleteUser(Long id); // 회원 탈퇴
}