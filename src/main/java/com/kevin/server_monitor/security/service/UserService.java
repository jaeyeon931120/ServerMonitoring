package com.kevin.server_monitor.security.service;

import com.kevin.server_monitor.security.mapper.UserMapper;
import com.kevin.server_monitor.security.vo.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserMapper userMapper;

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

        if (!userVo.getId().isEmpty()) {
            // password는 암호화해서 DB에 저장
            String salt = getSalt();
            userVo.setSalt(salt);
            userVo.setPassword(getEncrypt(userVo.getPassword(), salt));
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
            String salt = getSalt();
            userVo.setSalt(salt);
            userVo.setPassword(getEncrypt(userVo.getPassword(), salt));
        }
        result = userMapper.updateUser(userVo);

        return result;
    }

    public int withdraw(String id) { // 회원 탈퇴
        int result;
        result = userMapper.deleteUser(id);

        return result;
    }

    public UserDetails getUserDetails(String id) {
        return userMapper.getUserDetails(id);
    }

    public void failCntUpdate(String id) {
        userMapper.failCntUpdate(id);
    }

    public Map<String, Object> getFailCnt(String id) { return userMapper.getFailCnt(id); }

    public void changeEnabled(String id) { userMapper.changeEnabled(id); }

    /* 무작이 문자열 Salt */
    public String getSalt() {

        //1. Random, salt 생성
        SecureRandom sr = new SecureRandom();
        byte[] salt = new byte[20];

        //2. 난수 생성
        sr.nextBytes(salt);

        //3. byte To String(10진수 문자열로 변경)
        StringBuilder sb = new StringBuilder();
        for(byte b : salt) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }

    public String getEncrypt(String pwd, String salt) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            md.update((pwd + salt).getBytes());
            byte[] pwdSalt = md.digest();

            StringBuilder sb = new StringBuilder();
            for(byte b : pwdSalt) {
                sb.append(String.format("%02x", b));
            }

            result = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            logger.error("password hashing algorithm to error, java error : {}", e.getMessage());
        }

        return result;
    }
}
