package com.ihb.mytalentbackend.dto.talent;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TalentResponseDTO {

    private Long id;
    private Long userId;
    private String title;
    private String category;
    private String description;
    private Integer creditPerHour;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long thumbnailId;
    private String thumbnailUrl;

}
