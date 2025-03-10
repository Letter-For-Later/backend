package org.example.letter.domain.eliceAi.controller;

import java.util.stream.Collectors;
import org.example.letter.domain.eliceAi.dto.ChatRequest;
import org.example.letter.domain.eliceAi.dto.ChatResponse;
import org.example.letter.domain.eliceAi.dto.Message;
import org.example.letter.domain.eliceAi.service.EmotionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/letters")
public class EmotionController {

    private final EmotionService emotionService;

    public EmotionController(EmotionService emotionService) {
        this.emotionService = emotionService;
    }

    @PostMapping("/emotion")
    public ResponseEntity<ChatResponse> analyzeLetterSentiment(@RequestBody ChatRequest chatRequest) {
        // 각 메시지의 content만 추출하여 하나의 문자열로 결합
        String letterContent = chatRequest.getMessages().stream()
                .map(Message::getContent)
                .collect(Collectors.joining(" "));

        ChatResponse chatResponse = emotionService.analyzeSentiment(letterContent);
        return ResponseEntity.ok(chatResponse);
    }
}