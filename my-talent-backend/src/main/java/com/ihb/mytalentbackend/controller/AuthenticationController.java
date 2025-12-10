package com.ihb.mytalentbackend.controller;

import com.ihb.mytalentbackend.domain.User;
import com.ihb.mytalentbackend.service.AuthenticationService;
import com.ihb.mytalentbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "http://localhost:5173")   // 프론트 주소

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody User user) {

        if (userService.findUserByEmail(user.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 이메일입니다.");
        }

        if (userService.findUserByUserId(user.getUserId()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 아이디입니다.");
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.saveUser(user));
    }


    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody User user) {

        User signedIn = authenticationService.signInAndReturnJWT(user);

        return ResponseEntity.ok(
                java.util.Map.of(
                        "id", signedIn.getId(),
                        "email", signedIn.getEmail(),
                        "nickname", signedIn.getNickname(),
                        "role", signedIn.getRole().name(),
                        "token", signedIn.getToken()
                )
        );
    }

}
