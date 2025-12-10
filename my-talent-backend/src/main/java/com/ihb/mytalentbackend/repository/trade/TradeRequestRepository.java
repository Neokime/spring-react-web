package com.ihb.mytalentbackend.repository.trade;

import com.ihb.mytalentbackend.domain.trade.TradeRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TradeRequestRepository extends JpaRepository<TradeRequest, Long> {

    // 특정 게시글의 신청 목록
    List<TradeRequest> findByTradeBoard_Id(Long tradeBoardId);

    // 하나의 게시글에서 특정 유저의 요청
    List<TradeRequest> findByTradeBoard_IdAndRequester_Id(Long tradeBoardId, Long userId);
}
