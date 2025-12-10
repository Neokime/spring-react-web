package com.ihb.mytalentbackend.dto.store;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PurchaseResponseDTO {

    private Long id;
    private Long userId;
    private Long itemId;

    private String itemTitle;
    private Integer usedCredit;

    private String purchasedAt;
}
