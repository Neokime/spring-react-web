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


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;
    @Column(length = 100)
    private Long userId;
    @Column(length = 500)
    private String message;// 신청 시간(몇 시간)
    @Column(nullable = false)
    private Integer hours;
    @Column(name = "total_credits", nullable = false)
    private Integer totalCredits;
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private TalentRequestStatus status;
    @CreationTimestamp
    @Column(name = "requested_at", nullable = false, updatable = false)
    private LocalDateTime requestedAt;
    @Column(name = "processed_at")
    private LocalDateTime processedAt;


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
