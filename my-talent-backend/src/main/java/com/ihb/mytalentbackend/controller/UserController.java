package com.ihb.mytalentbackend.controller;

import com.ihb.mytalentbackend.domain.Role;
import com.ihb.mytalentbackend.security.UserPrincipal;
import com.ihb.mytalentbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PutMapping("/change/{role}")
    public ResponseEntity<Object> changeRole(@AuthenticationPrincipal UserPrincipal principal,
                                             @PathVariable Role role) {
        // 파라미터 순서 뒤집기 (username, newRole)
        userService.changeRole(principal.getUsername(), role);
        return ResponseEntity.ok(true);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(true);
    }


}
