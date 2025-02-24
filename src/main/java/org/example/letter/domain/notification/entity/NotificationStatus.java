package org.example.letter.domain.notification.entity;

public enum NotificationStatus {
    PENDING("발송대기"),    // 발송 대기 상태
    SENT("발송완료"),      // 발송 성공
    FAILED("발송실패");    // 발송 실패

    private final String description;

    NotificationStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}