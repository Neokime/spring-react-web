// src/main/java/com/ihb/mytalentbackend/domain/talent/TalentRequest.java
package com.ihb.mytalentbackend.domain.talent;

import com.ihb.mytalentbackend.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"talent", "requester"})
@Table(name = "talent_request")
public class TalentRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 재능에 대한 신청인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "talent_id", nullable = false)
    private TalentBoard talent;

    // 누가 신청했는지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    // 신청 메세지
    @Column(length = 500)
    private String message;

    // 신청 시간(몇 시간)
    @Column(nullable = false)
    private Integer hours;

    // 총 크레딧 (시간 * 시간당 크레딧) - 계산해서 세팅
    @Column(name = "total_credits", nullable = false)
    private Integer totalCredits;

    // 상태: PENDING / ACCEPTED / REJECTED / CANCELED
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private TalentRequestStatus status;

    // 신청한 시각
    @CreationTimestamp
    @Column(name = "requested_at", nullable = false, updatable = false)
    private LocalDateTime requestedAt;

    // 처리된 시각(수락/거절 시점). 아직이면 null
    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    // === 편의 메서드(선택) ===
    public void accept() {
        this.status = TalentRequestStatus.ACCEPTED;
        this.processedAt = LocalDateTime.now();
    }

    public void reject() {
        this.status = TalentRequestStatus.REJECTED;
        this.processedAt = LocalDateTime.now();
    }

    public void cancel() {
        this.status = TalentRequestStatus.CANCELED;
        this.processedAt = LocalDateTime.now();
    }
}
