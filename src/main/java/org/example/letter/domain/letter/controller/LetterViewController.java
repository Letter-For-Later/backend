package org.example.letter.domain.letter.controller;

import io.swagger.v3.oas.annotations.Parameter;
import org.example.letter.domain.letter.docs.LetterViewControllerDocs;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import org.example.letter.domain.letter.service.LetterService;
import org.example.letter.domain.letter.entity.Letter;
import org.example.letter.domain.letter.dto.LetterResponseDTO;

@RestController
@RequestMapping("/letters")
@RequiredArgsConstructor
public class LetterViewController implements LetterViewControllerDocs {
    
    private final LetterService letterService;

    @GetMapping("/{letterId}/view/{notificationId}")
    public LetterResponseDTO.LetterDetailResponse viewLetter(
            @Parameter(description = "조회할 편지 ID", required = true) 
            @PathVariable Long letterId,
            @Parameter(description = "알림 ID (접근 권한 확인용)", required = true) 
            @PathVariable String notificationId
    ) {
        Letter letter = letterService.getLetterByIdAndNotificationId(letterId, notificationId);
        return LetterResponseDTO.LetterDetailResponse.from(letter);
    }
}
