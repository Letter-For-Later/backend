package org.example.letter.domain.notification.entity;

import lombok.Getter;

@Getter
public enum TimeStatus {
    MORNING("아침", 9),
    LUNCH("점심", 13),
    DINNER("저녁", 18);

    private final String description;
    private final int hour;

    TimeStatus(String description, int hour) {
        this.description = description;
        this.hour = hour;
    }

    public static TimeStatus fromString(String value) {
        try {
            return TimeStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("예약 가능 시간은 MORNING(아침), LUNCH(점심), DINNER(저녁) 중 하나여야 합니다.");
        }
    }
}
