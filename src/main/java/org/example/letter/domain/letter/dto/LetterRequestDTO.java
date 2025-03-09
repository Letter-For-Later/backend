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
import org.example.letter.domain.user.entity.User;
import org.example.letter.domain.notification.entity.TimeStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import jakarta.validation.constraints.AssertTrue;
import io.swagger.v3.oas.annotations.media.Schema;
import org.example.letter.global.config.AppProperties;

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

        public Letter toEntity(User user) {
            return Letter.builder()
                    .sender(sender)
                    .content(content)
                    .receiver(receiver)
                    .status(LetterStatus.DRAFT)
                    .user(user)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ReservationRequest {
        @Schema(description = "발신자 이름", example = "홍길동")
        @NotNull(message = "발신자를 입력해주세요.")
        @Size(max = LetterConstants.MAX_SENDER_LENGTH, message = "발신자는 {max}자를 초과할 수 없습니다.")
        private String sender;

        @Schema(description = "편지 내용", example = "안녕하세요. 잘 지내시나요?")
        @NotNull(message = "내용을 입력해주세요.")
        @Size(max = LetterConstants.MAX_CONTENT_LENGTH, message = "내용은 {max}자를 초과할 수 없습니다.")
        private String content;

        @Schema(description = "수신자 이름", example = "김철수")
        @NotNull(message = "수신자를 입력해주세요.")
        @Size(max = LetterConstants.MAX_RECEIVER_LENGTH, message = "수신자는 {max}자를 초과할 수 없습니다.")
        private String receiver;

        @Schema(
            description = "전화번호 (010으로 시작하는 11자리)", 
            example = "01012341234", 
            pattern = "^010\\d{4}\\d{4}$"
        )
        @NotNull(message = "전화번호를 입력해주세요.")
        @Pattern(regexp = "^010\\d{4}\\d{4}$", message = "전화번호는 010으로 시작하는 11자리 숫자여야 합니다.")
        private String phoneNumber;

        @Schema(description = "예약 날짜 (내일 이후)", example = "2025-03-01")
        @NotNull(message = "예약 날짜를 선택해주세요.")
        private LocalDate reservationDate;

        @Schema(
            description = """
                예약 시간 (문자열로 입력)
                - MORNING: 아침(9시)
                - LUNCH: 점심(13시)
                - DINNER: 저녁(18시)
                """, 
            example = "MORNING / LUNCH / DINNER",
            allowableValues = {"MORNING", "LUNCH", "DINNER"}
        )
        @NotNull(message = "예약 시간을 선택해주세요.")
        private String reservationTime;

        @AssertTrue(message = "예약은 내일 이후 날짜만 가능합니다.")
        private boolean isValidReservationDate() {
            if (reservationDate == null) return false;
            return reservationDate.isAfter(LocalDate.now()) && 
                   reservationDate.isBefore(LocalDate.now().plusYears(2));
        }

        @Builder
        public ReservationRequest(String sender, String content, String receiver,
                                String phoneNumber, LocalDate reservationDate,
                                String reservationTime) {
            this.sender = sender;
            this.content = content;
            this.receiver = receiver;
            this.phoneNumber = phoneNumber;
            this.reservationDate = reservationDate;
            this.reservationTime = reservationTime;
        }

        @JsonIgnore
        public LocalDateTime getReservationDateTime() {
            TimeStatus timeStatus = TimeStatus.fromString(reservationTime);
            return LocalDateTime.of(reservationDate, LocalTime.of(timeStatus.getHour(), 0));
        }

        public Letter toEntity(User user, AppProperties appProperties) {
            Letter letter = Letter.builder()
                    .sender(sender)
                    .content(content)
                    .receiver(receiver)
                    .status(LetterStatus.RESERVED)
                    .user(user)
                    .build();

            letter.reserve(phoneNumber, getReservationDateTime(), appProperties);

            return letter;
        }
    }
}