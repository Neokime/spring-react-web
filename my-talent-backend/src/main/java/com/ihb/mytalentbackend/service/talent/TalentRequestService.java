// src/main/java/com/ihb/mytalentbackend/service/talent/TalentRequestService.java
package com.ihb.mytalentbackend.service.talent;

import com.ihb.mytalentbackend.dto.talent.TalentRequestCreateDTO;
import com.ihb.mytalentbackend.dto.talent.TalentRequestResponseDTO;

import java.util.List;

public interface TalentRequestService {

    // 신청 생성
    TalentRequestResponseDTO createRequest(Long talentId, Long requesterId,
                                           TalentRequestCreateDTO dto);

    // 내가 신청한 것들
    List<TalentRequestResponseDTO> getMyRequests(Long requesterId);

    // 내 재능에 들어온 신청 목록
    List<TalentRequestResponseDTO> getRequestsForTalent(Long talentId, Long ownerId);
}
