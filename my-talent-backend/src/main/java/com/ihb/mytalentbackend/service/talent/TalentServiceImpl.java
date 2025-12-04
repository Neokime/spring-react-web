package com.ihb.mytalentbackend.service.talent;

import com.ihb.mytalentbackend.domain.User;
import com.ihb.mytalentbackend.domain.talent.TalentBoard;
import com.ihb.mytalentbackend.dto.common.PageRequestDTO;
import com.ihb.mytalentbackend.dto.common.PageResponseDTO;
import com.ihb.mytalentbackend.dto.talent.TalentRequestDTO;
import com.ihb.mytalentbackend.dto.talent.TalentResponseDTO;
import com.ihb.mytalentbackend.repository.UserRepository;
import com.ihb.mytalentbackend.repository.talent.TalentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TalentServiceImpl implements TalentService {

    private final TalentRepository talentRepository;
    private final UserRepository userRepository;

    // ========== CREATE (등록) ==========
    @Override
    public TalentResponseDTO createTalent(TalentRequestDTO request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저 없음"));

        TalentBoard entity = dtoToEntity(request, user);
        TalentBoard saved = talentRepository.save(entity);

        return entityToDto(saved);
    }

    // ========== READ (단건 조회) ==========
    @Override
    public TalentResponseDTO getTalent(Long id) {
        TalentBoard entity = talentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("재능 없음"));

        return entityToDto(entity);
    }

    // ========== READ (목록 조회 - 페이징) ==========
    @Override
    public PageResponseDTO<TalentResponseDTO> getTalentList(PageRequestDTO pageRequestDTO) {

        Pageable pageable = pageRequestDTO.getPageable("id"); // 정렬 기준 컬럼: id

        Page<TalentBoard> result = talentRepository.findAll(pageable);

        List<TalentResponseDTO> dtoList = result.getContent().stream()
                .map(this::entityToDto)
                .toList();

        int total = (int) result.getTotalElements();

        return PageResponseDTO.<TalentResponseDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }

    // ========== UPDATE (수정) ==========
    @Override
    public TalentResponseDTO updateTalent(Long id, TalentRequestDTO request, Long userId) {
        TalentBoard entity = talentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("재능 없음"));

        // 작성자 체크
        if (!entity.getUser().getId().equals(userId)) {
            throw new RuntimeException("수정 권한 없음");
        }

        entity.setTitle(request.getTitle());
        entity.setCategory(request.getCategory());
        entity.setDescription(request.getDescription());
        entity.setCreditPerHour(request.getCreditPerHour());
        entity.setStatus(request.getStatus());

        // dirty checking으로 자동 반영
        return entityToDto(entity);
    }

    // ========== DELETE (삭제) ==========
    @Override
    public void deleteTalent(Long id, Long userId) {
        TalentBoard entity = talentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("재능 없음"));

        // 작성자 체크
        if (!entity.getUser().getId().equals(userId)) {
            throw new RuntimeException("삭제 권한 없음");
        }

        talentRepository.delete(entity);
    }

    // ========== PRIVATE MAPPER (DTO ↔ Entity) ==========

    // DTO -> Entity
    private TalentBoard dtoToEntity(TalentRequestDTO dto, User user) {
        return TalentBoard.builder()
                .user(user)
                .title(dto.getTitle())
                .category(dto.getCategory())
                .description(dto.getDescription())
                .creditPerHour(dto.getCreditPerHour())
                .status(dto.getStatus())
                .build();
    }

    // Entity -> DTO
    private TalentResponseDTO entityToDto(TalentBoard entity) {
        return TalentResponseDTO.builder()
                .id(entity.getId())
                .userId(entity.getUser().getId())
                .title(entity.getTitle())
                .category(entity.getCategory())
                .description(entity.getDescription())
                .creditPerHour(entity.getCreditPerHour())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
