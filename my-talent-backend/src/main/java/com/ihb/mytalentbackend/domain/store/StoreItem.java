package com.ihb.mytalentbackend.domain.store;

import com.ihb.mytalentbackend.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;        // 상품명

    @Column(nullable = false)
    private String description;  // 설명

    @Column(nullable = false)
    private Integer price;       // 필요 크레딧

    @Column(nullable = false)
    private String itemType;     // 상품 종류 (BANNER, BOOST, CREDIT_PACK, OTHER)

    @Column(nullable = false)
    private String status;       // ACTIVE / INACTIVE
}
