package com.kevin.server_monitor.security.mapper;

import com.kevin.server_monitor.security.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {
    List<UserVo> getUserList(); // User 테이블 가져오기
    int insertUser(UserVo userVo); // 회원 가입
    UserVo getUserById(String id); // 로그인한 유저 정보 가져오기
    int updateUser(UserVo userVo); // 회원 정보 수정
    int deleteUser(String id); // 회원 탈퇴
    int getIDCheck(String id); // 중복 ID 체크
    UserDetails getUserDetails(String id); // 유저 존재 여부 확인
    void failCntUpdate(String id); // 실패횟수 update
    Map<String, Object> getFailCnt(String id); // 실패횟수, isEnabled 조회
    void changeEnabled(String id); // 계정 활성화 여부변경, 1이었으면 0으로 0이었으면 1로 바꾼다, 0은 false, 1은 true이다.
}