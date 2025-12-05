// src/main/java/com/ihb/mytalentbackend/domain/talent/TalentRequestStatus.java
package com.ihb.mytalentbackend.domain.talent;

public enum TalentRequestStatus {
    PENDING,    // 신청 대기
    ACCEPTED,   // 수락됨
    REJECTED,   // 거절됨
    CANCELED    // 신청자가 취소
}
