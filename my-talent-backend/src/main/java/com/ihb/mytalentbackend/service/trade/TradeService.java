package com.ihb.mytalentbackend.service.trade;

import com.ihb.mytalentbackend.dto.trade.TradeBoardRequestDTO;
import com.ihb.mytalentbackend.dto.trade.TradeBoardResponseDTO;
import com.ihb.mytalentbackend.dto.trade.TradeRequestDTO;
import com.ihb.mytalentbackend.dto.trade.TradeRequestResponseDTO;

import java.util.List;

public interface TradeService {

    // 게시글 생성
    TradeBoardResponseDTO createTrade(TradeBoardRequestDTO request, Long userId);

    // 게시글 단건 조회
    TradeBoardResponseDTO getTrade(Long id);

    // 전체 게시글 목록
    List<TradeBoardResponseDTO> getAllTrades();

    // 게시글 수정
    TradeBoardResponseDTO updateTrade(Long id, TradeBoardRequestDTO request, Long userId);

    // 게시글 삭제
    void deleteTrade(Long id, Long userId);

    // 교환 신청
    TradeRequestResponseDTO requestTrade(Long boardId, Long requesterId, TradeRequestDTO request);

    // 게시글에 달린 신청 목록
    List<TradeRequestResponseDTO> getTradeRequests(Long boardId);

    // 신청 수락
    void acceptTradeRequest(Long boardId, Long requestId, Long ownerId);
}
