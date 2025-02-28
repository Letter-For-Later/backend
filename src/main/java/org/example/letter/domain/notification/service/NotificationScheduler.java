package org.example.letter.domain.notification.service;

import lombok.RequiredArgsConstructor;
import org.example.letter.domain.notification.entity.Notification;
import org.example.letter.domain.notification.entity.NotificationStatus;
import org.example.letter.domain.notification.repository.NotificationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationScheduler {

    private final NotificationRepository notificationRepository;
    private final CoolSmsService coolSmsService;

    // @Scheduled(cron = "0 0 9,13,18 * * *") // 매일 9시, 13시, 18시에 실행
//    @Scheduled(cron = "0 20 21 * * *")
    @Transactional
    public void sendScheduledNotifications() {
        log.info("문자 발송 스케줄러 시작");
        LocalDateTime now = LocalDateTime.now();
        
        try {
            List<Notification> notifications = notificationRepository
                    .findAllByStatusAndReservationDateTimeBefore(NotificationStatus.PENDING, now);
            
            log.info("발송 대상 알림 수: {}", notifications.size());

            for (Notification notification : notifications) {
                try {
                    processNotification(notification);
                } catch (Exception e) {
                    log.error("알림 처리 중 오류 발생: {}", notification.getId(), e);
                }
            }
        } catch (Exception e) {
            log.error("스케줄러 실행 중 오류 발생", e);
        }
    }

    private void processNotification(Notification notification) {
        try {
            if (notification.isReadyToSend()) {
                String content = String.format(
                    "[Letter for Later] 편지가 도착했습니다.\n확인하기: %s",
                    notification.getAccessUrl()
                );
                coolSmsService.sendSms(notification.getPhoneNumber(), content);
                notification.markAsSent();
                log.info("문자 발송 성공: {}", notification.getId());
            }
        } catch (Exception e) {
            notification.markAsFailed(e.getMessage());
            log.error("문자 발송 실패: {}", notification.getId(), e);
        }
    }
} 