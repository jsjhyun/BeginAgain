package com.team3.global.exception;

import com.team3.user.entity.UserSignupDto;
import com.team3.user.exception.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

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

    @ExceptionHandler(PasswordExistsException.class)
    public String handlePasswordExistsException(PasswordExistsException ex, Model model) {
        model.addAttribute("userSignupDto", new UserSignupDto());
        model.addAttribute("signupError", ex.getErrorCode().getMessage());
        return "signup";
    }
}
