package com.ihb.mytalentbackend.repository.store;

import com.ihb.mytalentbackend.domain.store.PurchaseHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseHistoryRepository extends JpaRepository<PurchaseHistory, Long> {

    List<PurchaseHistory> findByUser_Id(Long userId);

    void deleteByItemId(Long itemId);

}
