package com.team3.user.controller;

import com.team3.user.entity.UserSignupDto;
import com.team3.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class SignupController {

    private final UserService userService;

    // 회원가입 페이지 이동
    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("userSignupDto", new UserSignupDto());
        return "signup";
    }

    // 회원가입 처리
    @PostMapping("/signup")
    public String signup(@ModelAttribute("userSignupDto") @Valid UserSignupDto userSignupDto, BindingResult bindingResult, Model model) {

        // 유효성 검사
        if (bindingResult.hasErrors()) {
            model.addAttribute("userSignupDto", userSignupDto);
            return "signup";
        }

        try {
            userService.signup(userSignupDto);
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("signupError", e.getMessage()); // 회원가입 실패 이유
            model.addAttribute("userSignupDto", userSignupDto); // 입력 데이터 유지
            return "signup";
        }
    }
}