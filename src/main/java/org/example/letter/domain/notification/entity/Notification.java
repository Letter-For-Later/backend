package org.example.letter.domain.notification.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.letter.domain.letter.entity.Letter;
import org.example.letter.global.config.AppProperties;
import org.example.letter.global.domain.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
    name = "notification",
    indexes = {
        @Index(
            name = "idx_notification_status_reservation",
            columnList = "status,reservation_date_time"
        )
    }
)
public class Notification extends BaseEntity {
    private static final int MAX_RETRY_COUNT = 3;  // 최대 재시도 횟수
    private static final Logger log = LoggerFactory.getLogger(Notification.class);

    @Id
    @Column(name = "notification_id")
    private String id;  // UUID 사용

    @OneToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "letter_id")
    private Letter letter;

    @NotNull
    @Column(name = "phone_number")
    private String phoneNumber;

    @NotNull
    @Column(name = "access_url")
    private String accessUrl;

    // 예약 시간
    @NotNull
    @Column(name = "reservation_date_time")
    private LocalDateTime reservationDateTime;

    // 실제로 전송된 시간
    @Column(name = "sent_date_time")
    private LocalDateTime sentDateTime;

    @NotNull
    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    @NotNull
    @Column(name = "retry_count")
    private int retryCount;

    @Column(name = "fail_reason", columnDefinition = "TEXT")
    private String failReason;

    @Builder
    private Notification(Letter letter, String phoneNumber, LocalDateTime reservationDateTime, AppProperties appProperties) {
        this.id = UUID.randomUUID().toString();
        this.letter = letter;
        this.phoneNumber = phoneNumber;
        this.reservationDateTime = reservationDateTime;
        this.status = NotificationStatus.PENDING;
        this.retryCount = 0;
        this.accessUrl = generateAccessUrl(appProperties);
    }

    private String generateAccessUrl(AppProperties appProperties) {
        log.debug("Domain: {}", appProperties.getDomain());  // 도메인 값 로깅
        log.debug("Letter ID: {}", this.letter.getId());     // letter ID 로깅
        
        String url = String.format("%s/letters/%d/view/%s",
            appProperties.getDomain(),
            this.letter.getId(),
            this.id
        );
        
        log.debug("Generated URL: {}", url);  // 생성된 전체 URL 로깅
        return url;
    }

    // 발송 가능 여부 확인
    public boolean isReadyToSend() {
        return canSend() && isTimeToSend();
    }

    // 상태 기반 발송 가능 여부
    private boolean canSend() {
        return this.status == NotificationStatus.PENDING ||
                (this.status == NotificationStatus.FAILED && canRetry());
    }

    // 예약 시간 도달 여부
    private boolean isTimeToSend() {
        return LocalDateTime.now().isAfter(reservationDateTime);
    }

    // 발송 성공 처리
    public void markAsSent() {
        this.status = NotificationStatus.SENT;
        this.sentDateTime = LocalDateTime.now();
        this.letter.markAsSent();
    }

    // 발송 실패 처리
    public void markAsFailed(String failReason) {
        if (failReason == null || failReason.trim().isEmpty()) {
            throw new IllegalArgumentException("실패 사유는 필수입니다.");
        }
        this.status = NotificationStatus.FAILED;
        this.failReason = failReason;
        this.retryCount++;
    }

    // 재시도 가능 여부 확인
    private boolean canRetry() {
        return this.status == NotificationStatus.FAILED && this.retryCount < MAX_RETRY_COUNT;
    }
}