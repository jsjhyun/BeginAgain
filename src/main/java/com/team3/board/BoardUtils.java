package com.team3.board;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
public class BoardUtils {

    // 세션에서 userId를 가져오고 검증하는 공통 메서드
    public Long checkUserSession(HttpSession session) {
        Object sessionUserIdValue = session.getAttribute("userId");
        if (sessionUserIdValue == null || sessionUserIdValue.toString().isEmpty()) {
            return null;  // 세션에 userId가 없으면 null 반환
        }
        return (Long) sessionUserIdValue;  // 세션에 userId가 있으면 Long로 변환하여 반환
    }

    public ResponseEntity<String> buildRedirectLogin(Long sessionUserId){
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/login"));
        return ResponseEntity.status(HttpStatus.SEE_OTHER).headers(headers).build();
    }


}
