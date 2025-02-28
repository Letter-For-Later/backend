// package org.example.letter.domain.notification.service;

// import org.example.letter.domain.notification.config.CoolSmsProperties;
// import org.junit.jupiter.api.DisplayName;  // 테스트 이름 표시용
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.TestConfiguration;
// import org.springframework.context.annotation.Bean;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.springframework.test.context.TestPropertySource;
// import org.springframework.test.context.junit.jupiter.SpringExtension;
// import org.springframework.context.annotation.Import;

// @ExtendWith(SpringExtension.class)
// @Import(CoolSmsServiceTest.TestConfig.class)
// @TestPropertySource(properties = {
//     "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration," +
//     "org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration"
// })
// class CoolSmsServiceTest {

//     @Autowired
//     private CoolSmsService coolSmsService;

//     @TestConfiguration
//     static class TestConfig {
//         @Bean
//         public CoolSmsProperties coolSmsProperties() {
//             CoolSmsProperties properties = new CoolSmsProperties();
//             properties.setApiKey(System.getenv("SMS_API_KEY"));
//             properties.setApiSecret(System.getenv("SMS_SECRET_KEY"));
//             properties.setSender(System.getenv("SMS_PHONE_NUMBER"));
//             return properties;
//         }

//         @Bean
//         public CoolSmsService coolSmsService(CoolSmsProperties properties) {
//             return new CoolSmsService(properties);
//         }
//     }

//     @Test
//     @DisplayName("SMS 발송 테스트")  // 테스트 결과에 표시될 이름
//     void testSendSms() {
//         // Given
//         String testPhoneNumber = System.getenv("SMS_PHONE_NUMBER");
//         String testMessage = "안녕하세요! 테스트 메시지입니다.";

//         // When & Then
//         coolSmsService.sendSms(testPhoneNumber, testMessage);
//         // 예외가 발생하지 않으면 테스트 성공
//     }
// }
