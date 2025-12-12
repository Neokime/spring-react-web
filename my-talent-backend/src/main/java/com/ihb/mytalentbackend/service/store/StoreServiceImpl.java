package com.ihb.mytalentbackend.service.store;

import com.ihb.mytalentbackend.domain.Role;
import com.ihb.mytalentbackend.domain.User;
import com.ihb.mytalentbackend.domain.store.PurchaseHistory;
import com.ihb.mytalentbackend.domain.store.StoreItem;
import com.ihb.mytalentbackend.dto.store.*;
import com.ihb.mytalentbackend.repository.UserRepository;
import com.ihb.mytalentbackend.repository.store.PurchaseHistoryRepository;
import com.ihb.mytalentbackend.repository.store.StoreItemRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StoreServiceImpl implements StoreService {

    private final StoreItemRepository storeItemRepository;
    private final PurchaseHistoryRepository purchaseHistoryRepository;
    private final UserRepository userRepository;

    // ======================
    //  🔹 생성
    // ======================
    @Override
    public StoreItemResponseDTO createItem(StoreItemRequestDTO request) {

        StoreItem item = StoreItem.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .price(request.getPrice())
                .itemType(request.getItemType())
                .status("ACTIVE")
                .build();

        StoreItem saved = storeItemRepository.save(item);
        return toStoreItemDto(saved);
    }

    // ======================
    // 🔹 전체 목록
    // ======================
    @Override
    public List<StoreItemResponseDTO> getAllItems() {
        return storeItemRepository.findAll().stream()
                .map(this::toStoreItemDto)
                .toList();
    }

    // ======================
    // 🔹 수정
    // ======================
    @Override
    public StoreItemResponseDTO updateItem(Long id, StoreItemRequestDTO request) {

        StoreItem item = storeItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상품 없음"));

        item.setTitle(request.getTitle());
        item.setDescription(request.getDescription());
        item.setPrice(request.getPrice());
        item.setItemType(request.getItemType());

        return toStoreItemDto(item);
    }

    // ======================
    // 🔹 삭제
    // ======================
    @Transactional
    public void deleteItem(Long itemId) {

        // 먼저 구매 기록 삭제
        purchaseHistoryRepository.deleteByItemId(itemId);

        // 이후 아이템 삭제
        storeItemRepository.deleteById(itemId);
    }


    // ======================
    // 🔥 구매 기능 - 크레딧 차감 없음(지금은 구조만)
    // ======================
    @Override
    public PurchaseResponseDTO purchaseItem(Long itemId, Long userId) {

        StoreItem item = storeItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("상품 없음"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저 없음"));

        // 🔹 관리자 여부 체크 (Role이 enum이라고 가정)
        boolean isAdmin = user.getRole() == Role.ADMIN;

        // 현재 크레딧 / 가격을 안전하게 int 로 꺼내기
        int currentCredit = user.getCredit() == null ? 0 : user.getCredit();
        int price = item.getPrice().intValue(); // price가 Integer여도 .intValue()는 문제 없음

        // ⭐ 관리자 아닌 경우에만 크레딧 체크
        if (!isAdmin && currentCredit < price) {
            throw new RuntimeException("크래딧이 부족합니다.");
        }

        // ⭐ 관리자 아닌 경우에만 크레딧 차감
        if (!isAdmin) {
            user.setCredit(currentCredit - price);
        }

        PurchaseHistory history = PurchaseHistory.builder()
                .user(user)
                .item(item)
                .usedCredit(price)
                .build();

        PurchaseHistory saved = purchaseHistoryRepository.save(history);
        return toPurchaseDto(saved);
    }



    // ======================
    // 🔹 내가 구매한 내역
    // ======================
    @Override
    public List<PurchaseResponseDTO> getMyPurchases(Long userId) {

        return purchaseHistoryRepository.findByUser_Id(userId)
                .stream()
                .map(this::toPurchaseDto)
                .toList();
    }

    // ======================
    // Mapper
    // ======================

    private StoreItemResponseDTO toStoreItemDto(StoreItem item) {
        return StoreItemResponseDTO.builder()
                .id(item.getId())
                .title(item.getTitle())
                .description(item.getDescription())
                .price(item.getPrice())
                .itemType(item.getItemType())
                .status(item.getStatus())
                .build();
    }

    private PurchaseResponseDTO toPurchaseDto(PurchaseHistory h) {
        return PurchaseResponseDTO.builder()
                .id(h.getId())
                .userId(h.getUser().getId())
                .itemId(h.getItem().getId())
                .itemTitle(h.getItem().getTitle())
                .usedCredit(h.getUsedCredit())
                .purchasedAt(h.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .build();
    }
}
