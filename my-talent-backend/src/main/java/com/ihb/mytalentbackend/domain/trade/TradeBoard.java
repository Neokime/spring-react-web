package com.ihb.mytalentbackend.domain.trade;

import com.ihb.mytalentbackend.domain.BaseEntity;
import com.ihb.mytalentbackend.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TradeBoard extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 교환 글 작성자

    private String giveTalent;   // 내가 가진 재능
    private String wantTalent;   // 내가 원하는 재능
    private String description;  // 상세 설명

    private String status; // 상태
}

