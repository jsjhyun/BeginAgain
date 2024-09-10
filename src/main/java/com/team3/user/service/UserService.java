package com.team3.user.service;

import com.team3.user.dto.MyPageDto;
import com.team3.user.entity.User;
import com.team3.user.dto.UserLoginDto;
import com.team3.user.dto.UserSignupDto;

public interface UserService {
    // 로그인 메서드
    User login(UserLoginDto loginDto);

    // 회원가입 메서드
    User signup(UserSignupDto signupDto);

    // 회원 정보 조회 메서드
    MyPageDto getMyPageById(Long userId);

    // 닉네임 수정 메서드
    void updateNickname(Long userId, String newNickname);

    // 회원 삭제 메서드
    void deleteUserById(Long userId);
}
