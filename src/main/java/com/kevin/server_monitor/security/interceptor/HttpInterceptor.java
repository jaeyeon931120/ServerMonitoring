package com.kevin.server_monitor.security.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class HttpInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String id = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // 요청 URL
        String url = request.getRequestURI();
        // 아래 URL이 아니라면 로그인 체크
        if(!(url.equals("login") || url.equals("auth") || url.equals("logout")) && (id == null || id.equals(""))){
            response.sendRedirect("/login");
            return false;
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
