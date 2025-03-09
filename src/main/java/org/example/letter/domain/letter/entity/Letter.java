package org.example.letter.domain.letter.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.letter.domain.letter.exception.LetterErrorCode;
import org.example.letter.domain.letter.exception.LetterException;
import org.example.letter.domain.notification.entity.Notification;
import org.example.letter.global.domain.BaseEntity;
import org.example.letter.domain.user.entity.User;
import org.example.letter.global.config.AppProperties;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "letter")
public class Letter extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "letter_id")
    private Long id;

    @Column(length = LetterConstants.MAX_SENDER_LENGTH)
    private String sender;

    @Column(columnDefinition = "TEXT", length = LetterConstants.MAX_CONTENT_LENGTH)
    private String content;

    @Column(length = LetterConstants.MAX_RECEIVER_LENGTH)
    private String receiver;

    @NotNull
    @Enumerated(EnumType.STRING)
    private LetterStatus status;

    @OneToOne(mappedBy = "letter", cascade = CascadeType.ALL)
    private Notification notification;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    private Letter(String sender, String content, String receiver, LetterStatus status, User user) {
        validateLetterCreation(sender, content, receiver, status);
        this.sender = sender;
        this.content = content;
        this.receiver = receiver;
        this.status = status;
        this.user = user;
    }

    private void validateLetterCreation(String sender, String content, String receiver, LetterStatus status) {
        if (status == LetterStatus.RESERVED) {
            validateForReservation(sender, content, receiver);
        } else if (status == LetterStatus.DRAFT) {
            validateForDraft(sender, content, receiver);
        }
    }

    private void validateForDraft(String sender, String content, String receiver) {
        if (sender != null && !sender.trim().isEmpty()) {
            validateSenderLength(sender);
        }
        if (receiver != null && !receiver.trim().isEmpty()) {
            validateReceiverLength(receiver);
        }
        if (content != null && !content.trim().isEmpty()) {
            validateContentLength(content);
        }
    }

    private void validateForReservation(String sender, String content, String receiver) {
        validateSenderRequired(sender);
        validateReceiverRequired(receiver);
        validateContentRequired(content);
        validateSenderLength(sender);
        validateReceiverLength(receiver);
        validateContentLength(content);
    }

    private void validateSenderRequired(String sender) {
        if (sender == null || sender.trim().isEmpty()) {
            throw new LetterException(LetterErrorCode.LETTER_SENDER_REQUIRED);
        }
    }

    private void validateSenderLength(String sender) {
        if (sender.length() > LetterConstants.MAX_SENDER_LENGTH) {
            throw new LetterException(LetterErrorCode.LETTER_SENDER_TOO_LONG);
        }
    }

    private void validateReceiverRequired(String receiver) {
        if (receiver == null || receiver.trim().isEmpty()) {
            throw new LetterException(LetterErrorCode.LETTER_RECEIVER_REQUIRED);
        }
    }

    private void validateReceiverLength(String receiver) {
        if (receiver.length() > LetterConstants.MAX_RECEIVER_LENGTH) {
            throw new LetterException(LetterErrorCode.LETTER_RECEIVER_TOO_LONG);
        }
    }

    private void validateContentRequired(String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new LetterException(LetterErrorCode.LETTER_CONTENT_REQUIRED);
        }
    }

    private void validateContentLength(String content) {
        if (content.length() > LetterConstants.MAX_CONTENT_LENGTH) {
            throw new LetterException(LetterErrorCode.LETTER_CONTENT_TOO_LONG);
        }
    }

    public void reserve(String phoneNumber, LocalDateTime reservationDateTime, AppProperties appProperties) {
        validateReservationDateTime(reservationDateTime);
        validateForReservation(this.sender, this.content, this.receiver);

        this.status = LetterStatus.RESERVED;
        this.notification = createNotification(phoneNumber, reservationDateTime, appProperties);
    }

    public void validateCancel() {
        if (this.status != LetterStatus.RESERVED) {
            throw new LetterException(LetterErrorCode.LETTER_INVALID_OPERATION);
        }
    }

    // notification 에서 사용
    public void markAsSent() {
        if (this.status != LetterStatus.RESERVED) {
            throw new LetterException(LetterErrorCode.LETTER_INVALID_OPERATION);
        }
        this.status = LetterStatus.SENT;
    }

    // notification 에서 사용
    // 알림톡을 읽었을 경우 읽음 처리 -> 일단 보류
//    public void markAsRead() {
//        if (this.status != LetterStatus.SENT) {
//            throw new LetterException(LetterErrorCode.LETTER_INVALID_OPERATION);
//        }
//        this.status = LetterStatus.READ;
//    }

    private void validateReservationDateTime(LocalDateTime reservationDateTime) {
        LocalDateTime startOfNextDay = LocalDateTime.now()
                .plusDays(1)
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);

        if (reservationDateTime.isBefore(startOfNextDay)) {
            throw new IllegalArgumentException("예약은 다음날부터 가능합니다.");
        }
    }

    private Notification createNotification(String phoneNumber, LocalDateTime reservationDateTime, AppProperties appProperties) {
        return Notification.builder()
                .letter(this)
                .phoneNumber(phoneNumber)
                .reservationDateTime(reservationDateTime)
                .appProperties(appProperties)
                .build();
    }
}