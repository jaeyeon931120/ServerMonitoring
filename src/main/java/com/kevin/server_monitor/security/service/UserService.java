package com.kevin.server_monitor.security.service;

import com.kevin.server_monitor.security.mapper.UserMapper;
import com.kevin.server_monitor.security.vo.UserVo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public List<UserVo> getUserList() {
        return userMapper.getUserList();
    }

    public UserVo getUserById(String id) {
        return userMapper.getUserById(id);
    }

    public int getIDCheck(String id) { return userMapper.getIDCheck(id); }

    public int signup(UserVo userVo) { // 사용자 추가
        int result = 0;

        if(userVo.getAuthor().contains("관리자")) {
            userVo.setAuthor("ADMIN");
        } else if(userVo.getAuthor().contains("일반 사용자")) {
            userVo.setAuthor("USER");
        }

        if (!userVo.getId().equals("")) {
            // password는 암호화해서 DB에 저장
            userVo.setPassword(passwordEncoder.encode(userVo.getPassword()));
            result = userMapper.insertUser(userVo);
        }
        return result;
    }

    public int edit(UserVo userVo) { // 회원 정보 수정
        int result;
        // password는 암호화해서 DB에 저장
        if(userVo.getAuthor().contains("관리자")) {
            userVo.setAuthor("ADMIN");
        } else if(userVo.getAuthor().contains("일반 사용자")) {
            userVo.setAuthor("USER");
        }

        if(userVo.getPassword() != null) {
            userVo.setPassword(passwordEncoder.encode(userVo.getPassword()));
        }
        result = userMapper.updateUser(userVo);

        return result;
    }

    public int withdraw(String id) { // 회원 탈퇴
        int result;
        result = userMapper.deleteUser(id);

        return result;
    }

    public PasswordEncoder passwordEncoder() {
        return this.passwordEncoder;
    }

    public UserDetails getUserDetails(String id) {
        return userMapper.getUserDetails(id);
    }
}
