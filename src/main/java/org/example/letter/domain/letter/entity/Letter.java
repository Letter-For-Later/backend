package org.example.letter.domain.letter.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.letter.domain.notification.entity.Notification;
import org.example.letter.global.domain.BaseEntity;
import org.example.letter.domain.user.entity.User;

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
        if (status == LetterStatus.RESERVED) {
            validateForReservation(sender, content, receiver);
        }

        this.sender = sender;
        this.content = content;
        this.receiver = receiver;
        this.status = status;
        this.user = user;
    }

    private void validateForReservation(String sender, String content, String receiver) {
        validateSender(sender);
        validateReceiver(receiver);
        validateContent(content);
    }

    private void validateSender(String sender) {
        if (sender == null || sender.trim().isEmpty()) {
            throw new IllegalArgumentException("예약을 위해서는 발신자 정보가 필요합니다.");
        }
        if (sender.length() > LetterConstants.MAX_SENDER_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("발신자는 %d자를 초과할 수 없습니다.", LetterConstants.MAX_SENDER_LENGTH)
            );
        }
    }

    private void validateReceiver(String receiver) {
        if (receiver == null || receiver.trim().isEmpty()) {
            throw new IllegalArgumentException("예약을 위해서는 수신자 정보가 필요합니다.");
        }
        if (receiver.length() > LetterConstants.MAX_RECEIVER_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("수신자는 %d자를 초과할 수 없습니다.", LetterConstants.MAX_RECEIVER_LENGTH)
            );
        }
    }

    private void validateContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("예약을 위해서는 내용이 필요합니다.");
        }
        if (content.length() > LetterConstants.MAX_CONTENT_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("내용은 %d자를 초과할 수 없습니다.", LetterConstants.MAX_CONTENT_LENGTH)
            );
        }
    }

    public void reserve(String phoneNumber, LocalDateTime reservationDateTime) {
        validateForReservation(this.sender, this.content, this.receiver);
        validateReservationDateTime(reservationDateTime);

        this.status = LetterStatus.RESERVED;
        this.notification = Notification.builder()
                .letter(this)
                .phoneNumber(phoneNumber)
                .reservationDateTime(reservationDateTime)
                .build();
    }

    // 예약 시간 검증 메서드 추가
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

    public void markAsSent() {
        if (this.notification != null) {
            this.notification.markAsSent();
        }
        this.status = LetterStatus.SENT;
    }

    public void markAsRead() {
        this.status = LetterStatus.READ;
    }
}