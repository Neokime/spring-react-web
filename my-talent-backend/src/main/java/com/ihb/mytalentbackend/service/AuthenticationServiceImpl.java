package com.ihb.mytalentbackend.service;

import com.ihb.mytalentbackend.domain.User;
import com.ihb.mytalentbackend.security.UserPrincipal;
import com.ihb.mytalentbackend.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @Override
    public User signInAndReturnJWT(User signInRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signInRequest.getEmail(),
                        signInRequest.getPassword()
                )
        );

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        String jwt = jwtProvider.generateToken(principal);

        User signedIn = principal.getUser();
        // token 필드를 User에 만들지 않았으니, 프론트에 따로 내려주고 싶다면 DTO로 빼는 게 좋음.
        // 지금은 그냥 로그인을 위해 principal.user 반환
        return signedIn;
    }
}
