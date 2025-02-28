package org.example.letter.domain.notification.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "coolsms")
public class CoolSmsProperties {
    private String apiKey;
    private String apiSecret;
    private String sender;  // 발신번호
}