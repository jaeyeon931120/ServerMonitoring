package com.kevin.server_monitor.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration //빈등록 (IoC관리)
@EnableWebSecurity //security 필터 등록
@EnableGlobalMethodSecurity(prePostEnabled = true) //특정 주소로 접근하면 권한 및 인증을 미리 체크하겠다는 뜻
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationFailureHandler userLoginFailHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 권한에 따라 허용하는 url 설정
        // /login, /signup 페이지는 모두 허용, 다른 페이지는 인증된 사용자만 허용
        http
                .authorizeRequests()
                    .antMatchers("/login", "/logout", "/resource/**").permitAll()
                    .anyRequest().authenticated()
                .and()
                    .csrf()
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()
                    // login 설정
                    .formLogin()
                    .loginPage("/login")    // GET 요청 (login form을 보여줌)
                    .loginProcessingUrl("/auth")    // POST 요청 (login 창에 입력한 데이터를 처리)
                    .failureHandler(userLoginFailHandler)
                    .usernameParameter("id")    // login에 필요한 id 설정 (default는 username)
                    .passwordParameter("password")    // login에 필요한 password 값을 password(default)로 설정
                    .defaultSuccessUrl("/monitoring") // login에 성공하면 /로 redirect
                .and()
                    // logout 설정
                    .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login");    // logout에 성공하면 /로 redirect

        return http.build();
    }
}
