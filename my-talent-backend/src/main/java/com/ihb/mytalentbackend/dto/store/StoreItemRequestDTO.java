package com.ihb.mytalentbackend.dto.store;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreItemRequestDTO {

    private String title;
    private String description;
    private Integer price;
    private String itemType;
}
