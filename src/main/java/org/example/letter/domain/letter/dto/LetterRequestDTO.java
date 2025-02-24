package org.example.letter.domain.letter.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.letter.domain.letter.entity.Letter;
import org.example.letter.domain.letter.entity.LetterConstants;
import org.example.letter.domain.letter.entity.LetterStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

public class LetterRequestDTO {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class DraftRequest {
        @Size(max = LetterConstants.MAX_SENDER_LENGTH, message = "발신자는 {max}자를 초과할 수 없습니다.")
        private String sender;

        @Size(max = LetterConstants.MAX_CONTENT_LENGTH, message = "내용은 {max}자를 초과할 수 없습니다.")
        private String content;

        @Size(max = LetterConstants.MAX_RECEIVER_LENGTH, message = "수신자는 {max}자를 초과할 수 없습니다.")
        private String receiver;

        @Builder
        public DraftRequest(String sender, String content, String receiver) {
            this.sender = sender;
            this.content = content;
            this.receiver = receiver;
        }

        public Letter toEntity() {
            return Letter.builder()
                    .sender(sender)
                    .content(content)
                    .receiver(receiver)
                    .status(LetterStatus.DRAFT)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ReservationRequest {
        @NotNull(message = "발신자를 입력해주세요.")
        @Size(max = LetterConstants.MAX_SENDER_LENGTH, message = "발신자는 {max}자를 초과할 수 없습니다.")
        private String sender;

        @NotNull(message = "내용을 입력해주세요.")
        @Size(max = LetterConstants.MAX_CONTENT_LENGTH, message = "내용은 {max}자를 초과할 수 없습니다.")
        private String content;

        @NotNull(message = "수신자를 입력해주세요.")
        @Size(max = LetterConstants.MAX_RECEIVER_LENGTH, message = "수신자는 {max}자를 초과할 수 없습니다.")
        private String receiver;

        @NotNull(message = "전화번호를 입력해주세요.")
        @Pattern(regexp = "^010[0-9]{8,9}$", message = "올바른 전화번호 형식이 아닙니다.")
        private String phoneNumber;

        @NotNull(message = "예약 날짜를 선택해주세요.")
        private LocalDate reservationDate;

        @NotNull(message = "예약 시간을 선택해주세요.")
        @Min(value = 0, message = "시간은 0-23 사이여야 합니다")
        @Max(value = 23, message = "시간은 0-23 사이여야 합니다")
        private Integer reservationHour;

        @Builder
        public ReservationRequest(String sender, String content, String receiver,
                                String phoneNumber, LocalDate reservationDate,
                                Integer reservationHour) {
            this.sender = sender;
            this.content = content;
            this.receiver = receiver;
            this.phoneNumber = phoneNumber;
            this.reservationDate = reservationDate;
            this.reservationHour = reservationHour;
        }

        @JsonIgnore
        public LocalDateTime getReservationDateTime() {
            return LocalDateTime.of(reservationDate, LocalTime.of(reservationHour, 0));
        }

        public Letter toEntity() {
            Letter letter = Letter.builder()
                    .sender(sender)
                    .content(content)
                    .receiver(receiver)
                    .status(LetterStatus.RESERVED)
                    .build();

            letter.reserve(phoneNumber, getReservationDateTime());

            return letter;
        }
    }
}