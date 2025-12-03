package com.ihb.mytalentbackend.security.jwt;

import com.ihb.mytalentbackend.security.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface JwtProvider {

    // 토큰 생성
    String generateToken(UserPrincipal userPrincipal);

    // HttpServletRequest에서 Authentication 꺼내기
    Authentication getAuthentication(HttpServletRequest request);

    // 토큰 유효성 검증
    boolean isTokenValid(HttpServletRequest request);
}
