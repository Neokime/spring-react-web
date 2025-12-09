package com.ihb.mytalentbackend.security.jwt;

import com.ihb.mytalentbackend.domain.User;
import com.ihb.mytalentbackend.repository.UserRepository;
import com.ihb.mytalentbackend.security.UserPrincipal;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Component
@lombok.RequiredArgsConstructor

public class JwtProviderImpl implements JwtProvider {

    private final UserRepository userRepository;

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-in-ms}")
    private Long jwtExpirationInMs;

    @Override
    public String generateToken(UserPrincipal userPrincipal) {
        String authorities = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setSubject(userPrincipal.getUser().getUserId())  // 🔥 userId 명시
                .claim("roles", authorities)
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }


    @Override
    public Authentication getAuthentication(HttpServletRequest request) {
        Claims claims = extractClaims(request);
        if (claims == null) {
            return null;
        }

        // 로그인 때 subject 에 userId(로그인 아이디)를 넣었으니까
        String userId = claims.getSubject();
        if (userId == null) {
            log.info("JWT subject(userId) is null");
            return null;
        }

        // 🔥 userId(문자열)로 진짜 User 엔티티를 DB에서 가져온다
        User user = userRepository.findByUserId(userId).orElse(null);

        if (user == null) {
            log.warn("JWT userId={} 에 해당하는 유저를 찾을 수 없습니다.", userId);
            return null;  // 인증 실패로 처리 → 필터가 401/403 처리함
        }


        // roles 클레임에서 권한 뽑기
        String roles = claims.get("roles", String.class);
        Collection<? extends GrantedAuthority> authorities = Collections.emptySet();

        if (roles != null && !roles.isBlank()) {
            authorities = Arrays.stream(roles.split(","))
                    .filter(r -> !r.isBlank())
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());
        }

        // 🔥 UserPrincipal 에 User 통째로 넣기 (id 포함)
        UserPrincipal principal = new UserPrincipal(user);

        return new UsernamePasswordAuthenticationToken(
                principal,
                null,
                authorities
        );
    }



    @Override
    public boolean isTokenValid(HttpServletRequest request) {
        Claims claims = extractClaims(request);
        if (claims == null) {
            return false;
        }
        Date exp = claims.getExpiration();
        return exp != null && exp.after(new Date());
    }

    private Claims extractClaims(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            return null;
        }

        String token = header.substring(7);
        try {
            Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("JWT parsing failed: {}", e.getMessage());
            return null;
        }
    }
}
