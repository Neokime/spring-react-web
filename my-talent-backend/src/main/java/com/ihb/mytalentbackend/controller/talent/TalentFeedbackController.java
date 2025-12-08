package com.ihb.mytalentbackend.controller.talent;


import com.ihb.mytalentbackend.dto.talent.TalentFeedbackDTO;
import com.ihb.mytalentbackend.service.talent.TalentFeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/talents/{talentId}/feedback")
public class TalentFeedbackController {

    private final TalentFeedbackService feedbackService;

    // 피드백 등록
    @PostMapping
    public TalentFeedbackDTO createFeedback(
            @PathVariable Long talentId,
            @RequestBody TalentFeedbackDTO dto
    ) {
        dto.setTalentId(talentId);
        return feedbackService.createTalentFeedback(dto);
    }

    // 피드백 목록 조회
    @GetMapping
    public List<TalentFeedbackDTO> getFeedbacks(@PathVariable Long talentId) {
        return feedbackService.getFeedbacksByTalentId(talentId);
    }
}

