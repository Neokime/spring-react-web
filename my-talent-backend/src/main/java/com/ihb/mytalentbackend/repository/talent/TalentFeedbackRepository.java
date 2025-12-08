package com.ihb.mytalentbackend.repository.talent;

import com.ihb.mytalentbackend.domain.talent.TalentFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TalentFeedbackRepository extends JpaRepository<TalentFeedback, Long> {

    // 특정 talent 게시글에 달린 모든 피드백 조회
    List<TalentFeedback> findByTalentId(Long talentId);

    // 특정 유저가 특정 게시글에 이미 피드백을 남겼는지 체크 (선택)
    boolean existsByTalentIdAndUserId(Long talentId, Long userId);
}

