package com.kevin.server_monitor.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private UserService userService;
    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    /** id이 DB에 존재하는지 확인 **/
    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        UserDetails userDetails = userService.getUserDetails(id);

        if(userDetails == null) {
            throw new UsernameNotFoundException("유효하지 않는 계정입니다. 관리자에게 연락해주세요.");
        }

        return userDetails;
    }
}
