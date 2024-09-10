package com.team3.user.controller;

import com.team3.user.entity.User;
import com.team3.user.dto.UserLoginDto;
import com.team3.user.service.UserServiceImpl;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final UserServiceImpl userService;

    // 로그인 페이지 이동
    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }
    // 로그인 요청 처리
    @PostMapping("/login")
    public String login(@ModelAttribute UserLoginDto userLoginDto, HttpSession session) {
        User loginResult = userService.login(userLoginDto);
        session.setAttribute("userId", loginResult.getId()); // id를 세션에 저장
        return "redirect:/board";
    }
    // 로그아웃 요청 처리
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 무효화
        return "redirect:/board";
    }
}