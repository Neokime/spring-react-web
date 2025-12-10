package com.ihb.mytalentbackend.repository.trade;


import com.ihb.mytalentbackend.domain.trade.TradeBoard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TradeBoardRepository extends JpaRepository<TradeBoard, Long> {

    // 특정 사용자가 올린 교환 글 목록
    List<TradeBoard> findByUser_Id(Long userId);

    // 상태별 조회 (OPEN, MATCHED 등)
    List<TradeBoard> findByStatus(String status);
}
