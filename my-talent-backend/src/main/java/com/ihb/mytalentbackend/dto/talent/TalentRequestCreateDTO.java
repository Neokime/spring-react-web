// src/main/java/com/ihb/mytalentbackend/dto/talent/TalentRequestCreateDTO.java
package com.ihb.mytalentbackend.dto.talent;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TalentRequestCreateDTO {

    private Long userId;
    private String message;
    private Integer hours;
}
