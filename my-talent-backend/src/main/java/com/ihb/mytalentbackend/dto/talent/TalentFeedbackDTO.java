package com.ihb.mytalentbackend.dto.talent;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TalentFeedbackDTO {

    private Long id;            // 조회 시 사용, 등록 시 null
    private Long talentId;      // 조회 시 포함, 등록 시 pathVariable로 받아도 OK
    private Long userId;        // 조회 시 포함, 등록 시 Security에서 자동 처리도 가능

    private String nickname;    // 조회용

    private int rating;         // 등록 + 조회 공통
    private String content;     // 등록 + 조회 공통

    private String createdAt;   // 조회용
}
