package com.team3.user.controller;

import com.team3.user.dto.MyPageDto;
import com.team3.user.exception.NicknameExistsException;
import com.team3.user.service.UserServiceImpl;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class UserController {

    private final UserServiceImpl userService;

    // 회원 정보 폼
    @GetMapping("/{id}")
    public String myPageForm(@PathVariable("id") Integer userId, HttpSession session, Model model) {
        Integer sessionUserId = (Integer) session.getAttribute("userId");

        if (sessionUserId == null || !sessionUserId.equals(userId)) {
            return "redirect:/user/login";
        }

        MyPageDto userMyPageDto = userService.getMyPageById(userId);
        model.addAttribute("userMyPageDto", userMyPageDto);
        return "user/mypage";
    }

    // 회원 탈퇴 처리
    @PostMapping("/delete/{id}")
    public String deleteMyPage(@PathVariable("id") Integer userId, HttpSession session) {
        userService.deleteUserById(userId);
        session.invalidate();
        return "redirect:/board";
    }

    // 회원 정보 수정 처리
    @PostMapping("/update/{id}")
    public String updateMyPage(@PathVariable("id") Integer userId,
                               @RequestParam("nickname") String newNickname,
                               Model model) {
        try {
            userService.updateNickname(userId, newNickname);
        } catch (NicknameExistsException e) {
            model.addAttribute("nicknameError", e.getMessage());
            MyPageDto userMyPageDto = userService.getMyPageById(userId);
            model.addAttribute("userMyPageDto", userMyPageDto);
            return "user/mypage";
        }
        return "redirect:user/mypage/" +userId;
    }
}