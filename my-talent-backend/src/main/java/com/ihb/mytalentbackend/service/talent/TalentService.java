package com.ihb.mytalentbackend.service.talent;

import com.ihb.mytalentbackend.dto.common.PageRequestDTO;
import com.ihb.mytalentbackend.dto.common.PageResponseDTO;
import com.ihb.mytalentbackend.dto.talent.TalentRequestDTO;
import com.ihb.mytalentbackend.dto.talent.TalentResponseDTO;

public interface TalentService {

    // 생성
    TalentResponseDTO createTalent(TalentRequestDTO request, Long userId);

    // 단건 조회
    TalentResponseDTO getTalent(Long id);

    // 목록 조회 (Pageable 대신 PageRequestDTO / PageResponseDTO 사용)
    PageResponseDTO<TalentResponseDTO> getTalentList(PageRequestDTO pageRequestDTO);

    // 수정
    TalentResponseDTO updateTalent(Long id, TalentRequestDTO request, Long userId);

    // 삭제
    void deleteTalent(Long id, Long userId);
}
