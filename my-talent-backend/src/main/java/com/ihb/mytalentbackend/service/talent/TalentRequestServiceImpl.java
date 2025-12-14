// src/main/java/com/ihb/mytalentbackend/service/talent/TalentRequestServiceImpl.java
package com.ihb.mytalentbackend.service.talent;

import com.ihb.mytalentbackend.domain.Role;
import com.ihb.mytalentbackend.domain.talent.TalentBoard;
import com.ihb.mytalentbackend.domain.talent.TalentRequest;
import com.ihb.mytalentbackend.domain.talent.TalentRequestStatus;
import com.ihb.mytalentbackend.domain.User;
import com.ihb.mytalentbackend.dto.talent.TalentRequestCreateDTO;
import com.ihb.mytalentbackend.dto.talent.TalentRequestResponseDTO;
import com.ihb.mytalentbackend.repository.talent.TalentRepository;
import com.ihb.mytalentbackend.repository.talent.TalentRequestRepository;
import com.ihb.mytalentbackend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TalentRequestServiceImpl implements TalentRequestService {

    private final TalentRequestRepository talentRequestRepository;
    private final TalentRepository talentRepository;
    private final UserRepository userRepository;

    // ================= 신청 생성 =================
    @Override
    public TalentRequestResponseDTO createRequest(Long talentId,
                                                  Long requesterId,
                                                  TalentRequestCreateDTO dto) {

        // 1. 재능 조회
        TalentBoard talent = talentRepository.findById(talentId)
                .orElseThrow(() -> new RuntimeException("재능을 찾을 수 없습니다."));

        // 2. 신청자 조회
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        // 3. 시간 검증
        if (dto.getHours() == null || dto.getHours() <= 0) {
            throw new RuntimeException("신청 시간(hours)은 1 이상이어야 합니다.");
        }

        // 4. 총 크레딧 계산 (시간당 크레딧 * 신청 시간)
        int totalCredits = talent.getCreditPerHour() * dto.getHours();

        // 5. 엔티티 생성
        TalentRequest entity = TalentRequest.builder()
                .talent(talent)
                .requester(requester)
                .message(dto.getMessage())
                .hours(dto.getHours())
                .totalCredits(totalCredits)
                .status(TalentRequestStatus.PENDING)
                .build();

        // 6. 저장
        TalentRequest saved = talentRequestRepository.save(entity);

        // 7. DTO로 변환해서 반환
        return entityToDto(saved);
    }

    // ================= 내가 신청한 목록 =================
    @Override
    public List<TalentRequestResponseDTO> getMyRequests(Long requesterId) {
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        return talentRequestRepository.findByRequester(requester).stream()
                .map(this::entityToDto)
                .toList();
    }

    // ================= 내 재능에 들어온 신청들 =================
    @Override
    public List<TalentRequestResponseDTO> getRequestsForTalent(Long talentId, Long ownerId) {

        TalentBoard talent = talentRepository.findById(talentId)
                .orElseThrow(() -> new RuntimeException("재능을 찾을 수 없습니다."));

        // 작성자 확인 (본인 재능만 조회 가능)
        if (!talent.getUser().getId().equals(ownerId)) {
            throw new RuntimeException("해당 재능의 신청을 조회할 권한이 없습니다.");
        }

        return talentRequestRepository.findByTalent(talent).stream()
                .map(this::entityToDto)
                .toList();
    }

    @Override
    public void acceptRequest(Long talentId, Long requestId, Long ownerId) {

        TalentRequest request = talentRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("요청을 찾을 수 없습니다."));

        // 1) 재능이 맞는지 검증
        if (!request.getTalent().getId().equals(talentId)) {
            throw new RuntimeException("재능 ID가 일치하지 않습니다.");
        }

        // 2) 재능 소유자가 맞는지 검증 (판매자)
        if (!request.getTalent().getUser().getId().equals(ownerId)) {
            throw new RuntimeException("이 재능을 관리할 권한이 없습니다.");
        }


        User buyer = request.getRequester();      // 신청자
        User seller = request.getTalent().getUser(); // 판매자

        int buyerCredit = (buyer.getCredit() == null) ? 0 : buyer.getCredit();
        int sellerCredit = (seller.getCredit() == null) ? 0 : seller.getCredit();
        int totalCredits = request.getTotalCredits();



// 부족 여부 체크
        if (buyer.getRole() != Role.ADMIN) {
            buyerCredit = buyer.getCredit() == null ? 0 : buyer.getCredit();
            if (buyerCredit < totalCredits) {
                throw new RuntimeException("구매자의 크래딧이 부족합니다.");
            }
        }


// 구매자 차감
        buyer.setCredit(buyerCredit - totalCredits);

// 판매자 증가
        seller.setCredit(sellerCredit + totalCredits);

// 상태 변경
        request.accept();

        // @Transactional 이면 별도 save 없어도 flush 시 반영됨

        request.getTalent().setStatus("CLOSED");
    }



    @Override
    public void rejectRequest(Long talentId, Long requestId, Long ownerId) {

        TalentRequest request = talentRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("요청을 찾을 수 없습니다."));

        if (!request.getTalent().getId().equals(talentId)) {
            throw new RuntimeException("재능 ID가 일치하지 않습니다.");
        }

        if (!request.getTalent().getUser().getId().equals(ownerId)) {
            throw new RuntimeException("이 재능을 관리할 권한이 없습니다.");
        }

        request.reject();
        talentRequestRepository.save(request);
    }



    private TalentRequestResponseDTO entityToDto(TalentRequest entity) {
        return TalentRequestResponseDTO.builder()
                .id(entity.getId())
                .talentId(entity.getTalent().getId())
                .requesterId(entity.getRequester().getId())
                .message(entity.getMessage())
                .hours(entity.getHours())
                .totalCredits(entity.getTotalCredits())
                .status(entity.getStatus().name())
                .requestedAt(entity.getRequestedAt())
                .processedAt(entity.getProcessedAt())
                .build();
    }
}
