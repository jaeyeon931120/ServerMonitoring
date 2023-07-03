package com.kevin.server_monitor.security.service;

import com.kevin.server_monitor.security.mapper.UserMapper;
import com.kevin.server_monitor.security.vo.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public List<UserVo> getUserList() {
        return userMapper.getUserList();
    }

    public UserVo getUserByPK(Long pk) {
        return userMapper.getUserByPk(pk);
    }

    public UserVo getUserById(String id) {
        return userMapper.getUserById(id);
    }

    public void signup(UserVo userVo) { // 회원 가입
        if (!userVo.getId().equals("")) {
            // password는 암호화해서 DB에 저장
            userVo.setPassword(passwordEncoder.encode(userVo.getPassword()));
            userMapper.insertUser(userVo);
        }
    }

    public void edit(UserVo userVo) { // 회원 정보 수정
        // password는 암호화해서 DB에 저장
        userVo.setPassword(passwordEncoder.encode(userVo.getPassword()));
        userMapper.updateUser(userVo);
    }

    public void withdraw(Long id) { // 회원 탈퇴
        userMapper.deleteUser(id);
    }

    public PasswordEncoder passwordEncoder() {
        return this.passwordEncoder;
    }
}
