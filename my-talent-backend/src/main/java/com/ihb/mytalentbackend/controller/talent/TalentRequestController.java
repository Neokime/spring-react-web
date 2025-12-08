// src/main/java/com/ihb/mytalentbackend/controller/talent/TalentRequestController.java
package com.ihb.mytalentbackend.controller.talent;

import com.ihb.mytalentbackend.dto.talent.TalentRequestCreateDTO;
import com.ihb.mytalentbackend.dto.talent.TalentRequestResponseDTO;
import com.ihb.mytalentbackend.service.talent.TalentRequestService;
import com.ihb.mytalentbackend.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/talents")
@RequiredArgsConstructor
@Log4j2
public class TalentRequestController {

    private final TalentRequestService talentRequestService;

    // 공통으로 현재 로그인 유저 id 가져오기
    private Long getCurrentUserId(UserPrincipal principal) {
        if (principal == null || principal.getUser() == null) {
            // 인증 안 됐으면 401
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
        return principal.getUser().getId();
    }

    @PostMapping("/{talentId}/requests")
    public ResponseEntity<TalentRequestResponseDTO> createRequest(
            @PathVariable Long talentId,
            @RequestBody TalentRequestCreateDTO dto
    ) {
        Long requesterId = dto.getUserId();

        log.info("createRequest talentId={}, requesterId={}, dto={}",
                talentId, requesterId, dto);

        TalentRequestResponseDTO response =
                talentRequestService.createRequest(talentId, requesterId, dto);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/requests/me")
    public ResponseEntity<List<TalentRequestResponseDTO>> getMyRequests(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        Long userId = getCurrentUserId(principal);
        return ResponseEntity.ok(talentRequestService.getMyRequests(userId));
    }

    @GetMapping("/{talentId}/requests")
    public ResponseEntity<List<TalentRequestResponseDTO>> getRequestsForTalent(
            @PathVariable Long talentId,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        Long ownerId = getCurrentUserId(principal);
        return ResponseEntity.ok(talentRequestService.getRequestsForTalent(talentId, ownerId));
    }

    @PostMapping("/{talentId}/requests/{requestId}/accept")
    public ResponseEntity<?> acceptRequest(
            @PathVariable Long talentId,
            @PathVariable Long requestId,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        Long ownerId = getCurrentUserId(principal);
        talentRequestService.acceptRequest(talentId, requestId, ownerId);
        return ResponseEntity.ok("accepted");
    }

    @PostMapping("/{talentId}/requests/{requestId}/reject")
    public ResponseEntity<?> rejectRequest(
            @PathVariable Long talentId,
            @PathVariable Long requestId,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        Long ownerId = getCurrentUserId(principal);
        talentRequestService.rejectRequest(talentId, requestId, ownerId);
        return ResponseEntity.ok("rejected");
    }
}
