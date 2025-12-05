package com.ihb.mytalentbackend.controller.talent;

import com.ihb.mytalentbackend.dto.talent.TalentRequestCreateDTO;
import com.ihb.mytalentbackend.dto.talent.TalentRequestResponseDTO;
import com.ihb.mytalentbackend.security.UserPrincipal;
import com.ihb.mytalentbackend.service.talent.TalentRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/talents")
@RequiredArgsConstructor
@Log4j2
public class TalentRequestController {

    private final TalentRequestService talentRequestService;

    // 재능 신청 생성
    @PostMapping("/{talentId}/requests")
    public ResponseEntity<TalentRequestResponseDTO> createRequest(
            @PathVariable Long talentId,
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody TalentRequestCreateDTO dto
    ) {
        Long requesterId = principal.getUser().getId();

        log.info("talentId={}, requesterId={}, dto={}", talentId, requesterId, dto);

        TalentRequestResponseDTO response =
                talentRequestService.createRequest(talentId, requesterId, dto);

        return ResponseEntity.ok(response);
    }
}
