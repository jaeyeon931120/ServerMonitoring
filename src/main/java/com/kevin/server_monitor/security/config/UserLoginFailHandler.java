package com.kevin.server_monitor.security.config;

import com.kevin.server_monitor.security.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
public class UserLoginFailHandler implements AuthenticationFailureHandler {

    private final UserService userService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public UserLoginFailHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        String errorMessage;
        String loginId = request.getParameter("id");
        logger.info("Login onAuthenticationFailure ID : {}", loginId);

        if (exception instanceof UsernameNotFoundException) {
            errorMessage = "계정이 존재하지 않습니다. 관리자에게 문의해주세요.";
        } else if (exception instanceof BadCredentialsException) {
            boolean userUnlock;
            userUnlock = failCnt(loginId);
            if ( !userUnlock ) {
                errorMessage = "비밀번호가 맞지 않습니다. 다시 확인해 주세요. 5회 이상 틀릴경우, 계정이 잠깁니다.";
            } else {
                errorMessage = "계정이 잠겼습니다. KevinLAB 본사에 연락하여 계정 잠김을 풀어주세요.";
            }
        } else if (exception instanceof InternalAuthenticationServiceException) {
            errorMessage = "내부적으로 발생한 시스템 문제로 인해 요청을 처리할 수 없습니다. 관리자에게 문의하세요.";
        } else if (exception instanceof AuthenticationCredentialsNotFoundException) {
            errorMessage = "인증 요청이 거부되었습니다. 관리자에게 문의하세요.";
        } else {
            errorMessage = "알 수 없는 이유로 로그인에 실패하였습니다. 관리자에게 문의하세요.";
        }

        request.setAttribute("exception", errorMessage);
        String DEFAULT_FAILURE_URL = "/auth?error=true";
        request.getRequestDispatcher(DEFAULT_FAILURE_URL).forward(request, response);
    }

    private boolean failCnt(String loginId) {

        // 계정이 잠겼으면 추가로 실패횟수 증가시키지않고, true를 return한다.
        boolean userUnLock = true;
        Map<String, Object> account;

        // 실패횟수 select
        account = userService.getFailCnt(loginId);
        int failcnt = Integer.parseInt(account.get("failcnt").toString());
        int isenabled = Integer.parseInt(account.get("isenabled").toString());

        // 계정이 활성화 되어있는 경우에만 실패횟수와, Enabled설정을 변경한다.
        // Enabled설정은 실패횟수가 5이상일 때 바뀐다.
        if ( isenabled > 0 ) {
            if( failcnt < 5 ) {
                userService.failCntUpdate(loginId);
                userUnLock = false;
            } else {
                userService.changeEnabled(loginId);
            }
        }
        return userUnLock;
    }
}
