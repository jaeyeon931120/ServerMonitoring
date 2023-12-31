package com.kevin.server_monitor.security.config;

import com.kevin.server_monitor.security.service.UserService;
import com.kevin.server_monitor.security.vo.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthProvider implements AuthenticationProvider {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UserService userService;

    public AuthProvider(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String id = (String) authentication.getPrincipal(); // 로그인 창에 입력한 id
        String password = (String) authentication.getCredentials(); // 로그인 창에 입력한 password

        UsernamePasswordAuthenticationToken token;
        UserVo userVo = userService.getUserById(id);

        if (userVo != null) { // 일치하는 user 정보가 있는지 확인
            String salt = userVo.getSalt(); // 무작위 문자열 가져오기
            String hashingPw = userService.getEncrypt(password, salt);
            if (userVo.getPassword().equals(hashingPw)) {
                List<GrantedAuthority> roles = new ArrayList<>();
                roles.add(new SimpleGrantedAuthority(userVo.getAuthor())); // 권한 부여

                logger.info("User Sucessfully authenticated ID : {}, AUTHOR : {}", id, userVo.getAuthor());
                // 인증된 user 정보를 담아 SecurityContextHolder에 저장되는 token
                token = new UsernamePasswordAuthenticationToken(userVo.getId(), null, roles);

                return token;
            } else {
                throw new BadCredentialsException("비밀번호가 틀립니다.\n비밀번호를 확인 후 재 로그인 해주세요.");
            }
        } else {
            throw new UsernameNotFoundException("계정이 존재하지 않습니다.\n회원가입 진행 후 로그인 해주세요.");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}