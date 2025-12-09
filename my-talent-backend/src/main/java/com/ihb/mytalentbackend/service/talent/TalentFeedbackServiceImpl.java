package com.ihb.mytalentbackend.service.talent;

import com.ihb.mytalentbackend.domain.User;
import com.ihb.mytalentbackend.domain.talent.TalentBoard;
import com.ihb.mytalentbackend.domain.talent.TalentFeedback;
import com.ihb.mytalentbackend.dto.talent.TalentFeedbackDTO;
import com.ihb.mytalentbackend.repository.UserRepository;
import com.ihb.mytalentbackend.repository.talent.TalentFeedbackRepository;
import com.ihb.mytalentbackend.repository.talent.TalentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TalentFeedbackServiceImpl implements TalentFeedbackService {

    private final TalentFeedbackRepository feedbackRepository;
    private final TalentRepository talentBoardRepository;
    private final UserRepository userRepository;

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    public TalentFeedbackDTO createTalentFeedback(TalentFeedbackDTO dto) {

        // 1) talent, user 조회
        TalentBoard talent = talentBoardRepository.findById(dto.getTalentId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 재능 글입니다. id=" + dto.getTalentId()));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다. id=" + dto.getUserId()));

        // 2) DTO -> Entity
        TalentFeedback feedback = toEntity(dto, talent, user);

        // 3) save
        TalentFeedback saved = feedbackRepository.save(feedback);

        // 4) Entity -> DTO 후 리턴
        return toDto(saved);
    }

    @Override
    public List<TalentFeedbackDTO> getFeedbacksByTalentId(Long talentId) {

        List<TalentFeedback> feedbackList = feedbackRepository.findByTalentId(talentId);

        // 엔티티 리스트 → DTO 리스트 변환
        return feedbackList.stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public void updateFeedback(Long feedbackId, String content, int rating) {
        TalentFeedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new RuntimeException("피드백을 찾을 수 없습니다."));

        feedback.setContent(content);
        feedback.setRating(rating);

        feedbackRepository.save(feedback);
    }

    @Override
    public void deleteFeedback(Long feedbackId) {
        if (!feedbackRepository.existsById(feedbackId)) {
            throw new RuntimeException("피드백을 찾을 수 없습니다.");
        }
        feedbackRepository.deleteById(feedbackId);
    }


    // ====== 아래가 엔티티 <-> DTO 변환 ======

    private TalentFeedback toEntity(TalentFeedbackDTO dto, TalentBoard talent, User user) {
        return TalentFeedback.builder()
                .talent(talent)
                .user(user)
                .rating(dto.getRating())
                .content(dto.getContent())
                .build();
    }

    private TalentFeedbackDTO toDto(TalentFeedback feedback) {
        return TalentFeedbackDTO.builder()
                .id(feedback.getId())
                .talentId(feedback.getTalent().getId())
                .userId(feedback.getUser().getId())
                .nickname(feedback.getUser().getNickname())
                .rating(feedback.getRating())
                .content(feedback.getContent())
                .createdAt(
                        feedback.getCreatedAt() != null
                                ? feedback.getCreatedAt().format(DATE_TIME_FORMATTER)
                                : null
                )
                .build();
    }
}
