package com.ihb.mytalentbackend.controller.store;

import com.ihb.mytalentbackend.dto.store.*;
import com.ihb.mytalentbackend.security.UserPrincipal;
import com.ihb.mytalentbackend.service.store.StoreService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/store")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    // 🔹 상품 생성 (관리자)
    @PostMapping("/items")
    public StoreItemResponseDTO createItem(@RequestBody StoreItemRequestDTO request) {
        return storeService.createItem(request);
    }

    // 🔹 전체 상품 조회
    @GetMapping("/items")
    public List<StoreItemResponseDTO> getItems() {
        return storeService.getAllItems();
    }

    // 🔹 상품 수정
    @PutMapping("/items/{id}")
    public StoreItemResponseDTO updateItem(
            @PathVariable Long id,
            @RequestBody StoreItemRequestDTO request) {

        return storeService.updateItem(id, request);
    }

    // 🔹 상품 삭제
    @DeleteMapping("/items/{id}")
    public void deleteItem(@PathVariable Long id) {
        storeService.deleteItem(id);
    }

    // ==========================
    // 🔥 유저 구매
    // ==========================
    @PostMapping("/items/{id}/purchase")
    public PurchaseResponseDTO purchase(
            @PathVariable Long id,
            @org.springframework.security.core.annotation.AuthenticationPrincipal UserPrincipal principal) {

        Long userId = principal.getUser().getId();
        return storeService.purchaseItem(id, userId);
    }

    // 🔹 내가 구매한 내역 조회
    @GetMapping("/purchases")
    public List<PurchaseResponseDTO> myPurchases(
            @org.springframework.security.core.annotation.AuthenticationPrincipal UserPrincipal principal) {

        Long userId = principal.getUser().getId();
        return storeService.getMyPurchases(userId);
    }
}
