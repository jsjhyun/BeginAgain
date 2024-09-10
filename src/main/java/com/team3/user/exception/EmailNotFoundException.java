package com.team3.user.exception;

import com.team3.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class EmailNotFoundException extends RuntimeException {
    private final ErrorCode errorCode;
    // 이메일이 존재하지 않을 때
    public EmailNotFoundException() {
        super(ErrorCode.EMAIL_NOT_FOUND.getMessage());
        this.errorCode = ErrorCode.EMAIL_NOT_FOUND;
    }
}
