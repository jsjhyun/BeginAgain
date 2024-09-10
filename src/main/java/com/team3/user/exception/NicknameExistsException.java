package com.team3.user.exception;

import com.team3.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class NicknameExistsException extends RuntimeException {
    private final ErrorCode errorCode;
    // 닉네임 중복
    public NicknameExistsException() {
        super(ErrorCode.NICKNAME_ALREADY_EXISTS.getMessage());
        this.errorCode = ErrorCode.NICKNAME_ALREADY_EXISTS;
    }
}