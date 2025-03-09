package org.example.letter.domain.notification.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.example.letter.domain.notification.config.CoolSmsProperties;
import org.example.letter.domain.notification.exception.NotificationException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoolSmsService {
    
    private final CoolSmsProperties properties;
    private DefaultMessageService messageService;

    @PostConstruct
    public void init() {
        this.messageService = NurigoApp.INSTANCE.initialize(
            properties.getApiKey(),
            properties.getApiSecret(),
            "https://api.coolsms.co.kr"
        );
    }

    public void sendSms(String to, String content) {
        try {
            Message message = new Message();
            message.setFrom(properties.getSender());
            message.setTo(to);
            message.setText(content);

            var response = messageService.sendOne(new SingleMessageSendingRequest(message));
            
            if (!"2000".equals(response.getStatusCode())) {
                throw new NotificationException("문자 발송 실패: " + response.getStatusCode());
            }

            log.info("문자 발송 성공: {}", to);
        } catch (Exception e) {
            log.error("문자 발송 실패: {}", e.getMessage(), e);
            throw new NotificationException("문자 발송 실패", e);
        }
    }
}