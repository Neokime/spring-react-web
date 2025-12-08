package com.ihb.mytalentbackend.dto.talent;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TalentRequestDTO {

    private String title;
    private String category;
    private String description;
    private Integer creditPerHour;
    private String status;
}
