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
import org.springframework.security.crypto.password.PasswordEncoder;
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
        logger.info("Authentication start!");
        String id = (String) authentication.getPrincipal(); // 로그인 창에 입력한 id
        String password = (String) authentication.getCredentials(); // 로그인 창에 입력한 password

        PasswordEncoder passwordEncoder = userService.passwordEncoder();
        UsernamePasswordAuthenticationToken token;
        UserVo userVo = userService.getUserById(id);

        logger.info("userVo : {}", userVo);

        if (userVo != null && passwordEncoder.matches(password, userVo.getPassword())) { // 일치하는 user 정보가 있는지 확인
            List<GrantedAuthority> roles = new ArrayList<>();
            roles.add(new SimpleGrantedAuthority(userVo.getAuthor())); // 권한 부여

            token = new UsernamePasswordAuthenticationToken(userVo.getId(), null, roles);
            // 인증된 user 정보를 담아 SecurityContextHolder에 저장되는 token

            return token;
        } else if (userVo == null) {
            throw new UsernameNotFoundException("계정이 존재하지 않습니다.\n회원가입 진행 후 로그인 해주세요.");
        }

        throw new BadCredentialsException("No such user or wrong password.");
        // Exception을 던지지 않고 다른 값을 반환하면 authenticate() 메서드는 정상적으로 실행된 것이므로 인증되지 않았다면 Exception을 throw 해야 한다.
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}