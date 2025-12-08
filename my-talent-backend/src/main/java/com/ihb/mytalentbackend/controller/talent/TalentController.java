package com.ihb.mytalentbackend.controller.talent;

import com.ihb.mytalentbackend.dto.common.PageRequestDTO;
import com.ihb.mytalentbackend.dto.common.PageResponseDTO;
import com.ihb.mytalentbackend.dto.talent.TalentRequestDTO;
import com.ihb.mytalentbackend.dto.talent.TalentResponseDTO;
import com.ihb.mytalentbackend.service.UserService;
import com.ihb.mytalentbackend.service.talent.TalentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/talents")
public class TalentController {

    private final TalentService talentService;
    private final UserService userService;

    // 생성
    @PostMapping
    public TalentResponseDTO create(@RequestBody TalentRequestDTO request,
                                    @AuthenticationPrincipal User principal) {

        // principal.getUsername() = JwtProviderImpl에서 setSubject(...) 한 값 (email)
        String email = principal.getUsername();
        Long userId = userService.findUserByEmail(email).getId();

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
                                    @AuthenticationPrincipal User principal) {

        String email = principal.getUsername();
        Long userId = userService.findUserByEmail(email).getId();

        return talentService.updateTalent(id, request, userId);
    }

    // 삭제
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id,
                       @AuthenticationPrincipal User principal) {

        String email = principal.getUsername();
        Long userId = userService.findUserByEmail(email).getId();

        talentService.deleteTalent(id, userId);
    }
}
