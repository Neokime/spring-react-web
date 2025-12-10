package com.ihb.mytalentbackend.dto.trade;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TradeRequestResponseDTO {

    private Long id;
    private Long boardId;

    private Long requesterId;
    private String message;

    private String status;
}
