package com.kevin.server_monitor.security.vo;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {
    private final UserVo userVo; // 컴포지션

    public CustomUserDetails(UserVo userVo){
        this.userVo = userVo;
    }

    // 해당 User의 권한을 리턴하는곳.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add((GrantedAuthority) userVo::getAuthor);
        return collect;
    }


    // User 의 password 리턴
    @Override
    public String getPassword() {
        return userVo.getPassword();
    }

    @Override
    public String getUsername() {
        return userVo.getId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}