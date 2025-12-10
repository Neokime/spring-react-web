package com.ihb.mytalentbackend.dto.store;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StoreItemResponseDTO {
    private Long id;
    private String title;
    private String description;
    private Integer price;
    private String itemType;
    private String status;
}
