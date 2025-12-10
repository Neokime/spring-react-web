package com.ihb.mytalentbackend.service.trade;

import com.ihb.mytalentbackend.domain.User;
import com.ihb.mytalentbackend.domain.trade.TradeBoard;
import com.ihb.mytalentbackend.domain.trade.TradeRequest;
import com.ihb.mytalentbackend.dto.trade.TradeBoardRequestDTO;
import com.ihb.mytalentbackend.dto.trade.TradeBoardResponseDTO;
import com.ihb.mytalentbackend.dto.trade.TradeRequestDTO;
import com.ihb.mytalentbackend.dto.trade.TradeRequestResponseDTO;
import com.ihb.mytalentbackend.repository.UserRepository;
import com.ihb.mytalentbackend.repository.trade.TradeBoardRepository;
import com.ihb.mytalentbackend.repository.trade.TradeRequestRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TradeServiceImpl implements TradeService {

    private final TradeBoardRepository tradeBoardRepository;
    private final TradeRequestRepository tradeRequestRepository;
    private final UserRepository userRepository;

    // 게시글 생성
    @Override
    public TradeBoardResponseDTO createTrade(TradeBoardRequestDTO request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저 없음"));

        TradeBoard board = TradeBoard.builder()
                .user(user)
                .giveTalent(request.getGiveTalent())   // 🔥 엔티티 필드명과 맞춤
                .wantTalent(request.getWantTalent())   // 🔥
                .description(request.getDescription())
                .status("OPEN")
                .build();

        TradeBoard saved = tradeBoardRepository.save(board);

        return toBoardDto(saved);
    }

    // 단건 조회
    @Override
    public TradeBoardResponseDTO getTrade(Long id) {
        TradeBoard board = tradeBoardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("교환 글 없음"));

        return toBoardDto(board);
    }

    // 전체 목록
    @Override
    public List<TradeBoardResponseDTO> getAllTrades() {
        return tradeBoardRepository.findAll()
                .stream()
                .map(this::toBoardDto)
                .toList();
    }

    // 수정
    @Override
    public TradeBoardResponseDTO updateTrade(Long id, TradeBoardRequestDTO request, Long userId) {
        TradeBoard board = tradeBoardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("교환 글 없음"));

        if (!board.getUser().getId().equals(userId)) {
            throw new RuntimeException("수정 권한 없음");
        }

        board.setGiveTalent(request.getGiveTalent());
        board.setWantTalent(request.getWantTalent());
        board.setDescription(request.getDescription());

        // @Transactional 이라 dirty checking
        return toBoardDto(board);
    }

    // 삭제
    @Override
    public void deleteTrade(Long id, Long userId) {
        TradeBoard board = tradeBoardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("교환 글 없음"));

        if (!board.getUser().getId().equals(userId)) {
            throw new RuntimeException("삭제 권한 없음");
        }

        tradeBoardRepository.delete(board);
    }

    // 교환 신청
    @Override
    public TradeRequestResponseDTO requestTrade(Long boardId, Long requesterId, TradeRequestDTO request) {
        TradeBoard board = tradeBoardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("교환 글 없음"));

        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new RuntimeException("유저 없음"));

        TradeRequest tradeRequest = TradeRequest.builder()
                .tradeBoard(board)
                .requester(requester)
                .message(request.getMessage())
                .status("PENDING")
                .build();

        TradeRequest saved = tradeRequestRepository.save(tradeRequest);

        return toRequestDto(saved);
    }

    // 특정 글의 신청 목록
    @Override
    public List<TradeRequestResponseDTO> getTradeRequests(Long boardId) {
        return tradeRequestRepository.findByTradeBoard_Id(boardId)
                .stream()
                .map(this::toRequestDto)
                .toList();
    }

    // 신청 수락
    @Override
    public void acceptTradeRequest(Long boardId, Long requestId, Long ownerId) {
        TradeBoard board = tradeBoardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("교환 글 없음"));

        if (!board.getUser().getId().equals(ownerId)) {
            throw new RuntimeException("수락 권한 없음");
        }

        TradeRequest req = tradeRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("신청 없음"));

        if (!req.getTradeBoard().getId().equals(boardId)) {
            throw new RuntimeException("요청이 이 게시글에 속하지 않음");
        }

        // 해당 요청 ACCEPTED 로
        req.setStatus("ACCEPTED");

        // 게시글 상태도 MATCHED 로 변경
        board.setStatus("MATCHED");
    }

    // ================== Mapper ==================

    private TradeBoardResponseDTO toBoardDto(TradeBoard board) {
        return TradeBoardResponseDTO.builder()
                .id(board.getId())
                .userId(board.getUser().getId())
                .giveTalent(board.getGiveTalent())
                .wantTalent(board.getWantTalent())
                .description(board.getDescription())
                .status(board.getStatus())
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .build();
    }

    private TradeRequestResponseDTO toRequestDto(TradeRequest req) {
        return TradeRequestResponseDTO.builder()
                .id(req.getId())
                .boardId(req.getTradeBoard().getId())
                .requesterId(req.getRequester().getId())
                .message(req.getMessage())
                .status(req.getStatus())
                .build();
    }
}
