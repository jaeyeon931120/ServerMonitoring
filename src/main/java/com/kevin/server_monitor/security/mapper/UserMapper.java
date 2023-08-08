package com.kevin.server_monitor.security.mapper;

import com.kevin.server_monitor.security.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    List<UserVo> getUserList(); // User 테이블 가져오기
    int insertUser(UserVo userVo); // 회원 가입
    UserVo getUserById(String id); // 로그인한 유저 정보 가져오기
    int updateUser(UserVo userVo); // 회원 정보 수정
    int deleteUser(String id); // 회원 탈퇴
    int getIDCheck(String id); // 중복 ID 체크
}