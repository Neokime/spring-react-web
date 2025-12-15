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
                        signInRequest.getUserId(),
                        signInRequest.getPassword()
                )
        );

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        String jwt = jwtProvider.generateToken(principal);

        User signedIn = principal.getUser();

        //  credit 이 null 이면 0으로 맞춰주기
        if (signedIn.getCredit() == null) {
            signedIn.setCredit(0);
        }

        signedIn.setToken(jwt);
        return signedIn;
    }
}
