package com.team3.global.aop;

import com.team3.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    // 서비스 메서드가 실행되기 전에 로그를 출력
    @Before("execution(* com.team3.user.service.UserServiceImpl.*(..))")
    public void logBeforeUserMethod(JoinPoint joinPoint) {
        log.info("메소드 {} 실행 전: {}", joinPoint.getSignature().getName(), joinPoint.getArgs());
    }

    // 서비스 메서드가 정상적으로 실행된 후 로그를 출력
    @AfterReturning(pointcut = "execution(* com.team3.user.service.UserServiceImpl.*(..))", returning = "result")
    public void logAfterUserMethod(JoinPoint joinPoint, Object result) {
        if (result instanceof User) {
            User user = (User) result;
            log.info("메소드 {} 실행: User [email={}, username={}]",
                    joinPoint.getSignature().getName(),
                    user.getEmail(),
                    user.getUsername());
        } else {
            log.info("메소드 {} 실행 후", joinPoint.getSignature().getName());
        }
    }

    // 서비스 메서드 실행 중 예외가 발생했을 때 로그를 출력
    @AfterThrowing(pointcut = "execution(* com.team3.user.service.UserServiceImpl.*(..))", throwing = "error")
    public void logAfterThrowingUserMethod(JoinPoint joinPoint, Throwable error) {
        log.error("실행 메소드: {}. Error: {}", joinPoint.getSignature().getName(), error.getMessage());
    }
}