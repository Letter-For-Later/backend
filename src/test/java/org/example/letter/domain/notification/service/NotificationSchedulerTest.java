// package org.example.letter.domain.notification.service;

// import org.example.letter.domain.notification.entity.Notification;
// import org.example.letter.domain.notification.entity.NotificationStatus;
// import org.example.letter.domain.notification.repository.NotificationRepository;
// import org.example.letter.domain.letter.entity.Letter;
// import org.example.letter.domain.letter.entity.LetterStatus;
// import org.example.letter.global.config.AppProperties;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;

// import java.time.LocalDateTime;
// import java.util.Arrays;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.mockito.ArgumentMatchers.anyString;
// import static org.mockito.Mockito.*;

// @ExtendWith(MockitoExtension.class)
// class NotificationSchedulerTest {

//     @Mock
//     private NotificationRepository notificationRepository;

//     @Mock
//     private CoolSmsService coolSmsService;

//     @Mock
//     private AppProperties appProperties;

//     @InjectMocks
//     private NotificationScheduler notificationScheduler;

//     @Test
//     @DisplayName("예약된 알림 발송 테스트")
//     void testSendScheduledNotifications() {
//         // Given
//         LocalDateTime now = LocalDateTime.now();
        
//         Letter letter = Letter.builder()
//                 .sender("홍길동")
//                 .receiver("김철수")
//                 .content("테스트 내용")
//                 .status(LetterStatus.RESERVED)
//                 .build();

//         when(appProperties.getDomain()).thenReturn("http://test.com");

//         Notification notification = Notification.builder()
//                 .letter(letter)
//                 .phoneNumber("01012345678")
//                 .reservationDateTime(now.minusMinutes(1))
//                 .appProperties(appProperties)
//                 .build();

//         when(notificationRepository.findAllByStatusAndReservationDateTimeBefore(
//                 eq(NotificationStatus.PENDING), any(LocalDateTime.class)))
//                 .thenReturn(Arrays.asList(notification));

//         // When
//         notificationScheduler.sendScheduledNotifications();

//         // Then
//         verify(coolSmsService).sendSms(
//                 eq("01012345678"),
//                 contains("홍길동님께서 보내신 편지가 도착했습니다")
//         );
//         verify(notificationRepository, times(1))
//                 .findAllByStatusAndReservationDateTimeBefore(any(), any());
//     }

//     @Test
//     @DisplayName("SMS 발송 실패시 상태 업데이트 테스트")
//     void testSendFailure() {
//         // Given
//         LocalDateTime now = LocalDateTime.now();
        
//         Letter letter = Letter.builder()
//                 .sender("홍길동")
//                 .receiver("김철수")
//                 .content("테스트 내용")
//                 .status(LetterStatus.RESERVED)
//                 .build();

//         when(appProperties.getDomain()).thenReturn("http://test.com");

//         Notification notification = Notification.builder()
//                 .letter(letter)
//                 .phoneNumber("01012345678")
//                 .reservationDateTime(now.minusMinutes(1))
//                 .appProperties(appProperties)
//                 .build();

//         when(notificationRepository.findAllByStatusAndReservationDateTimeBefore(
//                 any(), any()))
//                 .thenReturn(Arrays.asList(notification));
//         doThrow(new RuntimeException("SMS 발송 실패"))
//                 .when(coolSmsService)
//                 .sendSms(anyString(), anyString());

//         // When
//         notificationScheduler.sendScheduledNotifications();

//         // Then
//         verify(coolSmsService).sendSms(anyString(), anyString());
//         assertEquals(NotificationStatus.FAILED, notification.getStatus());
//     }
// }
