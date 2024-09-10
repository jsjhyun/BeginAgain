package com.team3.user.exception;

import com.team3.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class EmailExistsException extends RuntimeException {
    private final ErrorCode errorCode;
    // 이메일 중복
    public EmailExistsException() {
        super(ErrorCode.EMAIL_ALREADY_EXISTS.getMessage());
        this.errorCode = ErrorCode.EMAIL_ALREADY_EXISTS;
    }
}