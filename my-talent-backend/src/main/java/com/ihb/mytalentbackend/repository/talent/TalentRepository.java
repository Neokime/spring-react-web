package com.ihb.mytalentbackend.repository.talent;

import com.ihb.mytalentbackend.domain.talent.TalentBoard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TalentRepository extends JpaRepository<TalentBoard, Long> {

    List<TalentBoard> findByUser_Id(Long userId);


}
