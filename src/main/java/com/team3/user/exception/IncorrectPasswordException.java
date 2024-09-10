package com.team3.user.exception;

import com.team3.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class IncorrectPasswordException extends RuntimeException {
    private final ErrorCode errorCode;
    // 비밀번호가 일치하지 않을 때
    public IncorrectPasswordException() {
        super(ErrorCode.INVALID_PASSWORD.getMessage());
        this.errorCode = ErrorCode.INVALID_PASSWORD;
    }
}
