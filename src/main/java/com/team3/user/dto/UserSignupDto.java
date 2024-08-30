package com.team3.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSignupDto {

    @Email(message = "올바른 이메일 형식을 입력해주세요.")
    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    @Size(min = 6, message = "비밀번호는 최소 6자리 이상이어야 합니다.")
    private String password;

    @NotBlank(message = "비밀번호 확인은 필수 입력 항목입니다.")
    private String passwordConfirm; // 비밀번호 확인

    @NotBlank(message = "이름은 필수 입력 항목입니다.")
    private String username;

    @NotBlank(message = "닉네임은 필수 입력 항목입니다.")
    private String nickname;

    // 비밀번호 일치 확인 메소드
    public boolean isPasswordConfirmed() {
        return this.password.equals(this.passwordConfirm);
    }
}