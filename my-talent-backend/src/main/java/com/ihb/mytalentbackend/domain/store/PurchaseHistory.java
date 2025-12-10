package com.ihb.mytalentbackend.domain.store;

import com.ihb.mytalentbackend.domain.BaseEntity;
import com.ihb.mytalentbackend.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;   // 구매자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private StoreItem item; // 구매한 상품

    @Column(nullable = false)
    private Integer usedCredit;  // 차감된 크레딧 값
}
