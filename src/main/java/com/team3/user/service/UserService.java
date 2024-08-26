package com.team3.user.service;

import com.team3.user.entity.MyPageDto;
import com.team3.user.entity.User;
import com.team3.user.entity.UserLoginDto;
import com.team3.user.entity.UserSignupDto;

public interface UserService {
    // 로그인 메서드
    User login(UserLoginDto loginDto);

    // 회원가입 메서드
    User signup(UserSignupDto signupDto);

    // 회원 정보 조회 메서드
    MyPageDto getMyPageById(Integer userId);

    // 닉네임 수정 메서드
    void updateNickname(Integer userId, String newNickname);

    // 회원 삭제 메서드
    void deleteUserById(Integer userId);
}
