package org.example.letter.domain.eliceAi.service;

import java.util.Arrays;
import java.util.UUID;
import org.example.letter.domain.eliceAi.dto.ChatRequest;
import org.example.letter.domain.eliceAi.dto.ChatResponse;
import org.example.letter.domain.eliceAi.dto.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmotionService {

    @Value("${alice.api.url}")
    private String aliceApiUrl;

    @Value("${alice.api.key}")
    private String aliceApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 편지 내용(letterContent)을 ALICE API에 보내 감정 분석 결과(ChatResponse)를 반환합니다.
     */
    public ChatResponse analyzeSentiment(String letterContent) {
        // 고유 세션 ID 생성
        String sessId = UUID.randomUUID().toString();

        // system 메시지: 감정 분석 요청 프롬프트
        Message systemMessage = new Message("system", "너는 감정 분석 전문가야. 아래 편지 내용을 분석하여 감정(예: 행복, 슬픔, 분노, 두려움, 놀람)과 간략한 감정 설명을 제공해줘.");
        // user 메시지: 실제 편지 내용
        Message userMessage = new Message("user", letterContent);

        // ALICE API 요청 객체 생성
        ChatRequest request = new ChatRequest();
        request.setModel("helpy-pro");
        request.setSess_id(sessId);
        request.setMessages(Arrays.asList(systemMessage, userMessage));

        // HTTP 헤더 설정 (Content-Type, Authorization)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + aliceApiKey);

        HttpEntity<ChatRequest> entity = new HttpEntity<>(request, headers);

        // ALICE API 호출 및 ChatResponse 반환
        ResponseEntity<ChatResponse> response = restTemplate.postForEntity(aliceApiUrl, entity, ChatResponse.class);

        if (response.getBody() != null) {
            return response.getBody();
        }
        // 오류 처리: 필요에 따라 예외를 던지거나 기본값을 반환할 수 있음
        return null;
    }
}
