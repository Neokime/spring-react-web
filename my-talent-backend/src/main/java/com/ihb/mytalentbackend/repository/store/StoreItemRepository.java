package com.ihb.mytalentbackend.repository.store;

import com.ihb.mytalentbackend.domain.store.StoreItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreItemRepository extends JpaRepository<StoreItem, Long> {
}
