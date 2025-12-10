package com.ihb.mytalentbackend.dto.trade;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TradeBoardResponseDTO {

    private Long id;
    private Long userId;

    private String giveTalent;   // 내가 가진 재능
    private String wantTalent;   // 내가 원하는 재능
    private String description;

    private String status;       // OPEN / MATCHED

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
