package com.team3.user.service;

import com.team3.user.dto.MyPageDto;
import com.team3.user.dto.UserLoginDto;
import com.team3.user.dto.UserSignupDto;
import com.team3.user.entity.*;
import com.team3.user.exception.*;
import com.team3.user.mapper.UserMapper;
import com.team3.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final BCryptPasswordService passwordEncoder;

    // 로그인
    public User login(UserLoginDto loginDto) {
        // 이메일로 사용자를 조회, 없으면 예외 처리
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(EmailNotFoundException::new);
        // 비밀번호가 일치하지 않으면 예외 처리
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new IncorrectPasswordException();
        }
        return user;
    }

    // 회원가입
    @Transactional
    public User signup(UserSignupDto signupDto) {
        // 중복 체크
        validateSignup(signupDto);
        // 비밀번호 암호화
        String encryptedPassword = passwordEncoder.encrypt(signupDto.getPassword());

        // 회원 저장
        User user = User.builder()
                .email(signupDto.getEmail())
                .password(encryptedPassword)
                .username(signupDto.getUsername())
                .nickname(signupDto.getNickname())
                .role(RoleType.USER)  // 기본 역할 (User)
                .build();

        return userRepository.save(user);
    }

    // 회원가입 중복 처리 메소드
    private void validateSignup(UserSignupDto signupDto){

        // 비밀번호 != 비밀번호 확인
        if (!signupDto.isPasswordConfirmed()) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        // 이메일 중복 체크
        if (userRepository.findByEmail(signupDto.getEmail()).isPresent()) {
            throw new EmailExistsException();
        }
        // 닉네임 중복 체크
        if (userRepository.findByNickname(signupDto.getNickname()).isPresent()) {
            throw new NicknameExistsException();
        }
    }
    // 회원 정보 조회
    public MyPageDto getMyPageById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을수 없습니다. " + userId));
        return UserMapper.toDTO(user);
    }

    // 닉네임 수정
    @Transactional
    public void updateNickname(Long userId, String newNickname) {
        if (userRepository.existsByNickname(newNickname)) {
            throw new NicknameExistsException();
        }
        // 사용 가능성 희박
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. " + userId));

        user.updateNickname(newNickname);
        userRepository.save(user);
    }

    // 회원 삭제
    @Transactional
    public void deleteUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. ID: " + userId));
        userRepository.delete(user);
    }
}