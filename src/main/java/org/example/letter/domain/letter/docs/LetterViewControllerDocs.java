package org.example.letter.domain.letter.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.letter.domain.letter.dto.LetterResponseDTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "수신자 편지 조회", description = "수신자의 일회성 편지 조회를 위한 API")
public interface LetterViewControllerDocs {

    @Operation(
        summary = "URL을 통한 편지 조회", 
        description = """
            수신자가 URL을 통해 편지를 조회합니다.
            - notification_id를 통해 해당 편지에 대한 접근 권한을 확인합니다.
            - 일회성 조회만 가능합니다.
            """
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(
                    value = """
                    {
                        "sender": "홍길동",
                        "content": "안녕하세요, 반갑습니다...",
                        "receiver": "김철수",
                        "createdAt": "2024-02-28T09:00:00"
                    }
                    """,
                    summary = "편지 상세 내용"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "편지를 찾을 수 없음",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(
                    value = "{\"code\": \"LETTER_404\", \"message\": \"편지를 찾을 수 없습니다.\"}"
                )
            )
        )
    })
    LetterResponseDTO.LetterDetailResponse viewLetter(
            @Parameter(description = "조회할 편지 ID", required = true) 
            @PathVariable Long letterId,
            @Parameter(description = "알림 ID (접근 권한 확인용)", required = true) 
            @PathVariable String notificationId
    );
}
