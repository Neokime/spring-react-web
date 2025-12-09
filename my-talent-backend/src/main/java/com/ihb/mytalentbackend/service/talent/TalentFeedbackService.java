package com.ihb.mytalentbackend.service.talent;

import com.ihb.mytalentbackend.dto.talent.TalentFeedbackDTO;

import java.util.List;

public interface TalentFeedbackService {

    // 피드백 생성
    TalentFeedbackDTO createTalentFeedback(TalentFeedbackDTO talentFeedbackDTO);

    // 특정 재능 글에 달린 피드백 목록 조회 (필요하면 사용)
    List<TalentFeedbackDTO> getFeedbacksByTalentId(Long talentId);

    void updateFeedback(Long feedbackId, String content, int rating);

    void deleteFeedback(Long feedbackId);
}
