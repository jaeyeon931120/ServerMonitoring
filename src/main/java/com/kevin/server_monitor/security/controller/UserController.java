package com.kevin.server_monitor.security.controller;

import com.kevin.server_monitor.security.service.UserService;
import com.kevin.server_monitor.security.vo.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home(Model model) { // 인증된 사용자의 정보를 보여줌
        String id = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // token에 저장되어 있는 인증된 사용자의 id 값

        UserVo userVo = userService.getUserById(id);
        userVo.setPassword(null); // password는 보이지 않도록 null로 설정

        model.addAttribute("user", userVo);
        model.addAttribute("id", id);
        model.addAttribute("username", userVo.getName());

        return "login/login";
    }

    @GetMapping("/login")
    public String loginPage() { // 로그인되지 않은 상태이면 로그인 페이지를, 로그인된 상태이면 home 페이지를 보여줌
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken)
            return "login/login";
        return "redirect:/";
    }

    @PostMapping("/auth")
    public String postLogin(HttpSession session, HttpServletRequest request) {
        String id = request.getParameter("id");
        String user = request.getParameter("user");
        String username = request.getParameter("username");

        session.setAttribute("id", id);
        session.setAttribute("user", user);
        session.setAttribute("username", username);

        return "login/login";
    }

    @GetMapping("/signup")
    public String signupPage() {  // 회원 가입 페이지
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken)
            return "login/signup";
        return "redirect:/";
    }

    @PostMapping("/signup")
    public String signup(UserVo userVo) { // 회원 가입
        try {
            userService.signup(userVo);
        } catch (DuplicateKeyException e) {
            return "redirect:/signup?error_code=-1";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/signup?error_code=-99";
        }
        return "redirect:/login";
    }

    @GetMapping("/loginupdate")
    public String editPage(Model model) { // 회원 정보 수정 페이지
        Long pk = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserVo userVo = userService.getUserByPK(pk);
        model.addAttribute("user", userVo);
        return "editPage";
    }

    @PostMapping("/loginupdate")
    public String edit(UserVo userVo) { // 회원 정보 수정
        Long pk = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userVo.setPk(pk);
        userService.edit(userVo);
        return "redirect:/";
    }

    @PostMapping("/logindelete")
    public String withdraw(HttpSession session) { // 회원 탈퇴
        Long pk = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (pk != null) {
            userService.withdraw(pk);
        }
        SecurityContextHolder.clearContext(); // SecurityContextHolder에 남아있는 token 삭제
        return "redirect:/";
    }
}