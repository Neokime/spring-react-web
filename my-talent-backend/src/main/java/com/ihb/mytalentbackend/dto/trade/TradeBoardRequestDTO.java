package com.ihb.mytalentbackend.dto.trade;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TradeBoardRequestDTO {

    private String giveTalent;   // 내가 가진 재능
    private String wantTalent;   // 내가 원하는 재능
    private String description;  // 상세 설명
}
