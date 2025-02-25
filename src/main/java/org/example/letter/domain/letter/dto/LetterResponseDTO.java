package org.example.letter.domain.letter.dto;

import lombok.Builder;
import lombok.Getter;
import org.example.letter.domain.letter.entity.Letter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class LetterResponseDTO {

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
        private LocalTime time;           // 시간
        private String recipientOrSender; // 수신자 또는 발신자
    }

    @Getter
    @Builder
    public static class LetterListResponse {
        private List<LetterListItem> letters;
        private int totalCount;

        public static LetterListResponse of(List<Letter> letters, boolean isReceived) {
            List<LetterListItem> items = letters.stream()
                    .map(letter -> LetterListItem.builder()
                            .letterId(letter.getId())
                            .date(letter.getNotification().getReservationDateTime().toLocalDate())
                            .time(letter.getNotification().getReservationDateTime().toLocalTime())
                            .recipientOrSender(isReceived ? letter.getSender() : letter.getReceiver())
                            .build())
                    .collect(Collectors.toList());

            return LetterListResponse.builder()
                    .letters(items)
                    .totalCount(items.size())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class LetterDetailResponse {
        private LocalDate reservationDate;    // 예약 날짜
        private LocalTime reservationTime;    // 예약 시간
        private String recipientOrSender;     // 수신자 또는 발신자
        private Long daysUntilReservation;    // 예약까지 남은 일수 (예약 편지만)
        private String content;               // 내용

        public static LetterDetailResponse fromReserved(Letter letter) {
            LocalDateTime reservationDateTime = letter.getNotification().getReservationDateTime();
            return LetterDetailResponse.builder()
                    .reservationDate(reservationDateTime.toLocalDate())
                    .reservationTime(reservationDateTime.toLocalTime())
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
                    .reservationTime(reservationDateTime.toLocalTime())
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
