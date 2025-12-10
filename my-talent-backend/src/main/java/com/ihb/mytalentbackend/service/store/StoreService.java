package com.ihb.mytalentbackend.service.store;

import com.ihb.mytalentbackend.dto.store.*;

import java.util.List;

public interface StoreService {

    StoreItemResponseDTO createItem(StoreItemRequestDTO request);

    List<StoreItemResponseDTO> getAllItems();

    StoreItemResponseDTO updateItem(Long id, StoreItemRequestDTO request);

    void deleteItem(Long id);

    // 구매 관련
    PurchaseResponseDTO purchaseItem(Long itemId, Long userId);

    List<PurchaseResponseDTO> getMyPurchases(Long userId);
}
