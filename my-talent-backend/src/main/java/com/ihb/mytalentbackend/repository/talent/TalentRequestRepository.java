package com.ihb.mytalentbackend.repository.talent;

import com.ihb.mytalentbackend.domain.User;
import com.ihb.mytalentbackend.domain.talent.TalentBoard;
import com.ihb.mytalentbackend.domain.talent.TalentRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TalentRequestRepository extends JpaRepository<TalentRequest, Long> {
    List<TalentRequest> findByRequester(User requester);
    List<TalentRequest> findByTalent(TalentBoard talent);
}


