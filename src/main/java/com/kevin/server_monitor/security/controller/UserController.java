package com.kevin.server_monitor.security.controller;

import com.kevin.server_monitor.security.service.UserService;
import com.kevin.server_monitor.security.vo.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.
                isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }

    @GetMapping("/")
    public String home(Model model) { // 인증된 사용자의 정보를 보여줌
        // token에 저장되어 있는 인증된 사용자의 id 값
        String id = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UserVo userVo = userService.getUserById(id);
        userVo.setPassword(null); // password는 보이지 않도록 null로 설정

        model.addAttribute("id", id);
        model.addAttribute("username", userVo.getUsername());

        if (isAuthenticated()) {
            return "redirect:/monitoring";
        }

        return "redirect:/login";
    }

    @GetMapping("/error/expired")
    public ModelAndView expiredPage() {
        logger.info("Expired page");
        return new ModelAndView("error/expired");
    }

    @GetMapping("/error/invalid")
    public ModelAndView invalidPage() {
        logger.info("Invalid page");
        return new ModelAndView("error/invalid");
    }

    // 로그인되지 않은 상태이면 로그인 페이지를, 로그인된 상태이면 home 페이지를 보여줌
    @RequestMapping("/login")
    public String loginPage(HttpServletRequest request, Model model) {

        Map<String, ?> redirectMap = RequestContextUtils.getInputFlashMap(request);

        if (redirectMap != null) {
            String error = (String) redirectMap.get("error");
            String exception = (String) redirectMap.get("exception");

            model.addAttribute("error", error);
            model.addAttribute("exception", exception);
        }

        if (isAuthenticated()) {
            return "redirect:/monitoring";
        }

        return "/login/login";
    }

    @PostMapping("/auth")
    public String postLogin(HttpServletRequest request,
                            @RequestParam(value = "error", required = false) String error,
                            RedirectAttributes redirectAttr) {
        String exception = request.getAttribute("exception").toString();
        HttpSession session = request.getSession(true);

        redirectAttr.addFlashAttribute("error", error);
        redirectAttr.addFlashAttribute("exception", exception);

        String id = request.getParameter("id");
        String username = request.getParameter("username");

        session.setAttribute("id", id);
        session.setAttribute("username", username);

        return "redirect:/login";
    }

    @PostMapping("/user_list")
    @ResponseBody
    public List<UserVo> getUserList() {
        List<UserVo> resultList = new ArrayList<>();

        try {
            resultList = userService.getUserList();
        } catch (Exception e) {
            logger.error("An error occurred while retrieving the user list from the DB. error message : {}", e.getMessage());
            e.printStackTrace();
        }
        return resultList;
    }

    @PostMapping("/user_plus")
    @ResponseBody
    public Map<String, Object> getUserPlus(@RequestBody UserVo userVo) { // 사용자 추가
        Map<String, Object> resultMap = new HashMap<>();

        try {
            int result = userService.signup(userVo);

            if (result > 0) {
                resultMap.put("result", "ok");
            } else {
                resultMap.put("result", "nok");
            }
        } catch (Exception e) {
            resultMap.put("result", "nok");
            logger.error("An error occurred while adding a user. error message : {}", e.getMessage());
            e.printStackTrace();
        }
        return resultMap;
    }

    @PostMapping("/user_edit")
    @ResponseBody
    public Map<String, Object> getUserEdit(@RequestBody UserVo userVo) { // 사용자 수정
        Map<String, Object> resultMap = new HashMap<>();

        try {
            int result = userService.edit(userVo);

            if (result > 0) {
                resultMap.put("result", "ok");
            } else {
                resultMap.put("result", "nok");
            }
        } catch (Exception e) {
            resultMap.put("result", "nok");
            logger.error("An error occurred while modifying the user. error message : {}", e.getMessage());
            e.printStackTrace();
        }
        return resultMap;
    }

    @PostMapping("/user_delete")
    @ResponseBody
    public List<Map<String, Object>> getUserDelete(@RequestBody List<UserVo> userVo) { // 회원 탈퇴
        List<Map<String, Object>> resultList = new ArrayList<>();
        Map<String, Object> resultMap = new HashMap<>();
        try {
            for (UserVo user : userVo) {
                resultMap = new HashMap<>();
                String id = user.getId();
                int result = userService.withdraw(id);

                if (result > 0) {
                    resultMap.put("result", "ok");
                    resultMap.put("id", id);
                } else {
                    resultMap.put("result", "nok");
                    resultMap.put("id", id);
                }
                resultList.add(resultMap);
            }
        } catch (Exception e) {
            resultMap.put("result", "error");
            resultList.add(resultMap);
            logger.error("An error occurred while deleting the user. error message : {}", e.getMessage());
            e.printStackTrace();
        }
        return resultList;
    }

    @GetMapping("/logout")
    public String userLogout(HttpServletRequest request, HttpServletResponse response) {

        for (Cookie cookie : request.getCookies()) {
            String cookieName = cookie.getName();
            Cookie cookieToDelete = new Cookie(cookieName, null);
            cookieToDelete.setMaxAge(0);
            response.addCookie(cookieToDelete);
        }

        return "redirect:/login";
    }
}