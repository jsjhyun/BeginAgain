package com.team3.global.exception;

import com.team3.board.exception.CustomException;
import com.team3.board.exception.ErrorResponseEntity;
import com.team3.user.dto.UserSignupDto;
import com.team3.user.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponseEntity> handleCustomException(CustomException e) {
        return ErrorResponseEntity.toResponseEntity(e.getErrorCode());
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public String handleEmailNotFoundException(EmailNotFoundException ex, Model model) {
        model.addAttribute("emailError", ex.getErrorCode().getMessage());
        return "login";
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public String handleIncorrectPasswordException(IncorrectPasswordException ex, Model model) {
        model.addAttribute("passwordError", ex.getErrorCode().getMessage());
        return "login";
    }

    @ExceptionHandler(EmailExistsException.class)
    public String handleEmailExistsException(EmailExistsException ex, Model model) {
        model.addAttribute("userSignupDto", new UserSignupDto());
        model.addAttribute("signupError", ex.getErrorCode().getMessage());
        return "signup";
    }

    @ExceptionHandler(NicknameExistsException.class)
    public String handleNicknameExistsException(NicknameExistsException ex, Model model) {
        model.addAttribute("userSignupDto", new UserSignupDto());
        model.addAttribute("signupError", ex.getErrorCode().getMessage());
        return "signup";
    }
}
