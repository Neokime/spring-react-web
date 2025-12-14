package com.ihb.mytalentbackend.controller.admin;

import com.ihb.mytalentbackend.domain.Role;
import com.ihb.mytalentbackend.domain.User;
import com.ihb.mytalentbackend.domain.talent.TalentBoard;
import com.ihb.mytalentbackend.repository.talent.TalentRepository;
import com.ihb.mytalentbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final TalentRepository talentRepository;

    // 🔥 전체 유저 조회
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    // 🔥 유저 권한 변경 (USER ↔ ADMIN)
    @PostMapping("/users/{id}/toggle-role")
    public ResponseEntity<Void> toggleUserRole(@PathVariable Long id) {

        // id로 유저 찾기 (기존 서비스 구조 최대한 그대로 활용)
        User user = userService.findAllUsers().stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        // 새 ROLE 결정
        Role newRole = (user.getRole() == Role.ADMIN) ? Role.USER : Role.ADMIN;

        // 🔥 비밀번호 다시 인코딩하지 않고, role만 업데이트
        userService.changeRole(user.getEmail(), newRole);

        return ResponseEntity.ok().build();
    }

    // 🔥 전체 재능 조회
    @GetMapping("/talents")
    public ResponseEntity<List<TalentBoard>> getAllTalents() {
        return ResponseEntity.ok(talentRepository.findAll());
    }

    // 🔥 재능 강제 비공개
    @PostMapping("/talents/{id}/close")
    public ResponseEntity<Void> closeTalent(@PathVariable Long id) {
        TalentBoard talent = talentRepository.findById(id).orElse(null);
        if (talent == null) {
            return ResponseEntity.notFound().build();
        }

        talent.setStatus("CLOSED");
        talentRepository.save(talent);

        return ResponseEntity.ok().build();
    }

    // 🔥 유저 삭제
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

}
