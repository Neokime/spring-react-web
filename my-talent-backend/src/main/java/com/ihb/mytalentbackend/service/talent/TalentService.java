package com.ihb.mytalentbackend.service.talent;

import com.ihb.mytalentbackend.dto.common.PageRequestDTO;
import com.ihb.mytalentbackend.dto.common.PageResponseDTO;
import com.ihb.mytalentbackend.dto.talent.TalentRequestDTO;
import com.ihb.mytalentbackend.dto.talent.TalentResponseDTO;

import java.util.List;

public interface TalentService {

    TalentResponseDTO createTalent(TalentRequestDTO request, Long userId);

    TalentResponseDTO getTalent(Long id);

    PageResponseDTO<TalentResponseDTO> getTalentList(PageRequestDTO pageRequestDTO);

    TalentResponseDTO updateTalent(Long id, TalentRequestDTO request, Long userId);

    void deleteTalent(Long id, Long userId);


    List<TalentResponseDTO> getTalentsByUser(Long userId);
}
