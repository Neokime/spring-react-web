package com.ihb.mytalentbackend.controller;

import com.ihb.mytalentbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


    @RestController
    @RequestMapping("/api/authentication")
    @RequiredArgsConstructor
    @CrossOrigin(origins = "*")
    public class AuthenticationController {

        private final AuthenticationService authenticationService;
        private final UserService userService;

        @PostMapping("sign-up")
        public ResponseEntity<Object> signUp(@RequestBody User user){
            if(userService.findUserByEmail(user.getEmail()) != null){
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(userService.saveUser(user), HttpStatus.CREATED);
        }

        @PostMapping("sign-in")
        public ResponseEntity<Object> signIn(@RequestBody User user){
            return new ResponseEntity<>(authenticationService.signInAndReturnJWT(user), HttpStatus.OK);
        }


}
