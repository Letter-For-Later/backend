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

//     @Scheduled(cron = "0 0 9,13,18 * * *") // 매일 9시, 13시, 18시에 실행
//    @Scheduled(cron = "0 20 21 * * *")
//    @Scheduled(cron = "0 * * * * *") // 매 분마다 체크
    @Transactional
    public void sendScheduledNotifications() {
        log.info("문자 발송 스케줄러 시작");
        
        LocalDateTime now = LocalDateTime.now();
        // 현재 시간의 분 시작과 끝을 계산
        LocalDateTime minuteStart = now.withSecond(0).withNano(0);
        LocalDateTime minuteEnd = minuteStart.plusMinutes(1);
        
        try {
            List<Notification> notifications = notificationRepository
                .findAllByStatusAndReservationDateTimeRange(
                    NotificationStatus.PENDING,
                    minuteStart,
                    minuteEnd
                );
            
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
                // URL을 별도 변수로 추출하고 공백 제거
                String url = notification.getAccessUrl().trim();
                
                String content = String.format(
                    "기다림 끝, 편지가 도착했어요!\n\n" +
                    "안녕하세요 %s님,\n" +
                    "%s님께서 보내신 특별한 편지가 도착했습니다.\n\n" +
                    "편지 확인 링크:\n" +
                    "\n%s\n\n" +  // URL 앞뒤로 빈 줄을 추가하고 단독으로 배치
                    "Letter for Later와 함께\n" +
                    "소중한 순간을 나눠보세요.",
                    notification.getLetter().getReceiver(),
                    notification.getLetter().getSender(),
                    url
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