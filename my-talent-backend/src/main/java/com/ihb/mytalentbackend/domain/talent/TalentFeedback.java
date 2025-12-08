package com.ihb.mytalentbackend.domain.talent;

import com.ihb.mytalentbackend.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "talent_feedback")
public class TalentFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 재능글(talent_board)에 대한 피드백인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "talent_id", nullable = false)
    private TalentBoard talent;   // ← 너가 쓰는 게시글 엔티티명에 맞춰서 수정

    // 누가 남긴 피드백인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 평점 (1~5점 정도로 사용)
    @Column(nullable = false)
    private int rating;

    // 피드백 내용
    @Column(nullable = false, length = 1000)
    private String content;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
