package org.example.letter.domain.letter.dto;

import lombok.Builder;
import lombok.Getter;
import org.example.letter.domain.letter.entity.Letter;
import org.example.letter.domain.notification.entity.TimeStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class LetterResponseDTO {

    // 공통으로 사용할 TimeStatus 변환 메서드
    private static TimeStatus convertToTimeStatus(LocalTime time) {
        int hour = time.getHour();
        if (hour == 9) return TimeStatus.MORNING;
        if (hour == 13) return TimeStatus.LUNCH;
        if (hour == 18) return TimeStatus.DINNER;
        // 기본값 또는 예외 처리
        return TimeStatus.MORNING; // 기본값 설정 또는 예외 처리 방식 선택
    }

    @Getter
    @Builder
    public static class LetterSummaryResponse {
        private int reservedCount;    // 예약 편지 개수
        private int sentCount;        // 보낸 편지 개수
        private int receivedCount;    // 받은 편지 개수
        private int draftCount;       // 임시 저장 개수
    }

    @Getter
    @Builder
    public static class LetterListItem {
        private Long letterId;
        private LocalDate date;           // 날짜
        private TimeStatus timeStatus;    // 시간 상태 (MORNING, LUNCH, DINNER)
        private String name;              // 수신자 또는 발신자
    }

    @Getter
    @Builder
    public static class LetterListResponse {
        private List<LetterListItem> letters;
        private boolean isReceived;
        //isReceived = true: 받은 편지 목록을 조회할 때 → 발신자(sender)의 이름을 보여줌
        //isReceived = false: 보낸 편지 목록을 조회할 때 → 수신자(receiver)의 이름을 보여줌

        public static LetterListResponse of(List<Letter> letters, boolean isReceived) {
            List<LetterListItem> items = letters.stream()
                    .map(letter -> LetterListItem.builder()
                            .letterId(letter.getId())
                            .date(letter.getNotification() != null ? 
                                    letter.getNotification().getReservationDateTime().toLocalDate() : null)
                            .timeStatus(letter.getNotification() != null ? 
                                    convertToTimeStatus(letter.getNotification().getReservationDateTime().toLocalTime()) : null)
                            .name(isReceived ? letter.getSender() : letter.getReceiver())
                            .build())
                    .collect(Collectors.toList());

            return LetterListResponse.builder()
                    .letters(items)
                    .isReceived(isReceived)
                    .build();
        }
    }

    @Getter
    @Builder
    public static class LetterDetailResponse {
        private LocalDate reservationDate;    // 예약 날짜
        private TimeStatus timeStatus;        // 시간 상태 (MORNING, LUNCH, DINNER)
        private String recipientOrSender;     // 수신자 또는 발신자
        private Long daysUntilReservation;    // 예약까지 남은 일수 (예약 편지만)
        private String content;               // 내용

        public static LetterDetailResponse fromReserved(Letter letter) {
            LocalDateTime reservationDateTime = letter.getNotification().getReservationDateTime();
            return LetterDetailResponse.builder()
                    .reservationDate(reservationDateTime.toLocalDate())
                    .timeStatus(convertToTimeStatus(reservationDateTime.toLocalTime()))
                    .recipientOrSender(letter.getReceiver())
                    .daysUntilReservation(ChronoUnit.DAYS.between(
                            LocalDate.now(), reservationDateTime.toLocalDate()))
                    .content(letter.getContent())
                    .build();
        }

        public static LetterDetailResponse fromSentOrReceived(Letter letter, boolean isReceived) {
            LocalDateTime reservationDateTime = letter.getNotification().getReservationDateTime();
            return LetterDetailResponse.builder()
                    .reservationDate(reservationDateTime.toLocalDate())
                    .timeStatus(convertToTimeStatus(reservationDateTime.toLocalTime()))
                    .recipientOrSender(isReceived ? letter.getSender() : letter.getReceiver())
                    .content(letter.getContent())
                    .build();
        }

        public static LetterDetailResponse fromDraft(Letter letter) {
            return LetterDetailResponse.builder()
                    .recipientOrSender(letter.getReceiver())
                    .content(letter.getContent())
                    .build();
        }
    }
}
