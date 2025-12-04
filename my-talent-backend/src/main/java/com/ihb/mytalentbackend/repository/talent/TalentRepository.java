package com.ihb.mytalentbackend.repository.talent;

import com.ihb.mytalentbackend.domain.talent.TalentBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TalentRepository extends JpaRepository<TalentBoard, Long> {
}
