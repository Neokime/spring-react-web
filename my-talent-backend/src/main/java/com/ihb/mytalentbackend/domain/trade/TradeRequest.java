package com.ihb.mytalentbackend.domain.trade;

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
public class TradeRequest extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trade_board_id", nullable = false)
    private TradeBoard tradeBoard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    private String message;
    private String status; // PENDING / ACCEPTED / REJECTED
}
