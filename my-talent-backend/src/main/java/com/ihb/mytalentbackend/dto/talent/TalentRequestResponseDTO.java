package com.ihb.mytalentbackend.dto.talent;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class TalentRequestResponseDTO {
    private Long id;
    private Long talentId;
    private Long requesterId;
    private String message;
    private Integer hours;
    private Integer totalCredits;
    private String status;
    private LocalDateTime requestedAt;
    private LocalDateTime processedAt;
}


