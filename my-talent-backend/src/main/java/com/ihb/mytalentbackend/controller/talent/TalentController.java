package com.ihb.mytalentbackend.controller.talent;

import com.ihb.mytalentbackend.dto.common.PageRequestDTO;
import com.ihb.mytalentbackend.dto.common.PageResponseDTO;
import com.ihb.mytalentbackend.dto.talent.TalentRequestDTO;
import com.ihb.mytalentbackend.dto.talent.TalentResponseDTO;
import com.ihb.mytalentbackend.security.UserPrincipal;   // ⭐ 추가
import com.ihb.mytalentbackend.service.talent.TalentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/talents")
public class TalentController {

    private final TalentService talentService;
    // private final UserService userService;  // ⭐ 이제 안 쓰이면 지워도 됨

    // 생성
    @PostMapping
    public TalentResponseDTO create(@RequestBody TalentRequestDTO request,
                                    @org.springframework.security.core.annotation.AuthenticationPrincipal UserPrincipal principal) {

        Long userId = principal.getUser().getId();  // ⭐ UserPrincipal 에서 바로 id 조회
        return talentService.createTalent(request, userId);
    }

    // 단건 조회
    @GetMapping("/{id}")
    public TalentResponseDTO get(@PathVariable Long id) {
        return talentService.getTalent(id);
    }

    // 목록 조회 (페이징)
    @GetMapping
    public PageResponseDTO<TalentResponseDTO> list(PageRequestDTO pageRequestDTO) {
        return talentService.getTalentList(pageRequestDTO);
    }

    // 수정
    @PutMapping("/{id}")
    public TalentResponseDTO update(@PathVariable Long id,
                                    @RequestBody TalentRequestDTO request,
                                    @org.springframework.security.core.annotation.AuthenticationPrincipal UserPrincipal principal) {

        Long userId = principal.getUser().getId();  // ⭐ 여기서도 동일
        return talentService.updateTalent(id, request, userId);
    }

    // 삭제
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id,
                       @org.springframework.security.core.annotation.AuthenticationPrincipal UserPrincipal principal) {

        Long userId = principal.getUser().getId();  // ⭐ 동일
        talentService.deleteTalent(id, userId);
    }

    // ⭐ 특정 유저가 등록한 재능 목록 조회
    @GetMapping("/user/{userId}")
    public List<TalentResponseDTO> getTalentsByUser(@PathVariable Long userId) {
        return talentService.getTalentsByUser(userId);
    }

}
