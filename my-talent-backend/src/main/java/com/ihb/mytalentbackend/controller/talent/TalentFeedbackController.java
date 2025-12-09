package com.ihb.mytalentbackend.controller.talent;


import com.ihb.mytalentbackend.dto.talent.TalentFeedbackDTO;
import com.ihb.mytalentbackend.service.talent.TalentFeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/talents/{talentId}/feedback")
public class TalentFeedbackController {

    private final TalentFeedbackService feedbackService;

    // 등록
    @PostMapping
    public TalentFeedbackDTO createFeedback(
            @PathVariable Long talentId,
            @RequestBody TalentFeedbackDTO dto
    ) {
        dto.setTalentId(talentId);
        return feedbackService.createTalentFeedback(dto);
    }

    // 조회
    @GetMapping
    public List<TalentFeedbackDTO> getFeedbacks(@PathVariable Long talentId) {
        return feedbackService.getFeedbacksByTalentId(talentId);
    }

    // 수정
    @PutMapping("/{feedbackId}")
    public ResponseEntity<Void> updateFeedback(
            @PathVariable Long talentId,
            @PathVariable Long feedbackId,
            @RequestBody TalentFeedbackDTO dto
    ) {
        feedbackService.updateFeedback(feedbackId, dto.getContent(), dto.getRating());
        return ResponseEntity.ok().build();
    }


    // 삭제
    @DeleteMapping("/{feedbackId}")
    public void deleteFeedback(
            @PathVariable Long talentId,
            @PathVariable Long feedbackId
    ) {
        feedbackService.deleteFeedback(feedbackId);
    }
}
