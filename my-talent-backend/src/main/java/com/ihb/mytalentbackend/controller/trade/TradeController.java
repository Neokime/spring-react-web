package com.ihb.mytalentbackend.controller.trade;

import com.ihb.mytalentbackend.dto.trade.TradeBoardRequestDTO;
import com.ihb.mytalentbackend.dto.trade.TradeBoardResponseDTO;
import com.ihb.mytalentbackend.dto.trade.TradeRequestDTO;
import com.ihb.mytalentbackend.dto.trade.TradeRequestResponseDTO;
import com.ihb.mytalentbackend.security.UserPrincipal;
import com.ihb.mytalentbackend.service.trade.TradeService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trades")
@RequiredArgsConstructor
public class TradeController {

    private final TradeService tradeService;


    // 1. 교환 게시글 생성

    @PostMapping
    public TradeBoardResponseDTO createTrade(
            @RequestBody TradeBoardRequestDTO request,
            @org.springframework.security.core.annotation.AuthenticationPrincipal UserPrincipal principal) {

        Long userId = principal.getUser().getId();
        return tradeService.createTrade(request, userId);
    }


    //  2. 단일 게시글 조회

    @GetMapping("/{id}")
    public TradeBoardResponseDTO getTrade(@PathVariable Long id) {
        return tradeService.getTrade(id);
    }


    //  3. 전체 교환 게시글 목록
    @GetMapping
    public List<TradeBoardResponseDTO> getAllTrades() {
        return tradeService.getAllTrades();
    }


    //  4. 교환 게시글 수정
    @PutMapping("/{id}")
    public TradeBoardResponseDTO updateTrade(
            @PathVariable Long id,
            @RequestBody TradeBoardRequestDTO request,
            @org.springframework.security.core.annotation.AuthenticationPrincipal UserPrincipal principal) {

        Long userId = principal.getUser().getId();
        return tradeService.updateTrade(id, request, userId);
    }


    //  5. 교환 게시글 삭제

    @DeleteMapping("/{id}")
    public void deleteTrade(
            @PathVariable Long id,
            @org.springframework.security.core.annotation.AuthenticationPrincipal UserPrincipal principal) {

        Long userId = principal.getUser().getId();
        tradeService.deleteTrade(id, userId);
    }

    // ==========================
//  6. 특정 게시글에 교환 신청하기
// ==========================
    @PostMapping("/{id}/requests")
    public TradeRequestResponseDTO requestTrade(
            @PathVariable Long id,
            @RequestBody TradeRequestDTO request,
            @org.springframework.security.core.annotation.AuthenticationPrincipal UserPrincipal principal) {

        Long requesterId = principal.getUser().getId();
        return tradeService.requestTrade(id, requesterId, request);
    }


    // ==========================
    //  7. 게시글 주인이 받은 신청 목록 조회
    // ==========================
    @GetMapping("/{id}/requests")
    public List<TradeRequestResponseDTO> getTradeRequests(@PathVariable Long id) {
        return tradeService.getTradeRequests(id);
    }

    // ==========================
    //  8. 교환 신청 수락
    // ==========================
    @PostMapping("/{id}/requests/{requestId}/accept")
    public void acceptTradeRequest(
            @PathVariable Long id,
            @PathVariable Long requestId,
            @org.springframework.security.core.annotation.AuthenticationPrincipal UserPrincipal principal) {

        Long ownerId = principal.getUser().getId();
        tradeService.acceptTradeRequest(id, requestId, ownerId);
    }
}
