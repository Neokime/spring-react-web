package com.ihb.mytalentbackend.controller;

import com.ihb.mytalentbackend.domain.User;
import com.ihb.mytalentbackend.service.AuthenticationService;
import com.ihb.mytalentbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<Object> signUp(@RequestBody User user) {
        if (userService.findUserByEmail(user.getEmail()) != null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(userService.saveUser(user), HttpStatus.CREATED);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<Object> signIn(@RequestBody User user) {
        // signInAndReturnJWT 안에서 인증 + JWT 생성
        User signedIn = authenticationService.signInAndReturnJWT(user);
        return new ResponseEntity<>(signedIn, HttpStatus.OK);
    }
}
