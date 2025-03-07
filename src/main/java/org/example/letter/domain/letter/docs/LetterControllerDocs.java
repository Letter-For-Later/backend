package org.example.letter.domain.letter.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.letter.domain.auth.auth.custom.CustomUserDetails;
import org.example.letter.domain.letter.dto.LetterRequestDTO;
import org.example.letter.domain.letter.dto.LetterResponseDTO;
import org.example.letter.global.config.swagger.ApiResponseConstants;
import org.example.letter.global.payload.CommonResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;

@Tag(name = "편지 API", description = "편지 예약, 조회, 관리를 위한 API")
public interface LetterControllerDocs {

    @Operation(
        summary = "예약 편지 저장", 
        description = """
            새로운 예약 편지를 저장합니다.
            - 예약 시간은 MORNING(아침 9시), LUNCH(점심 13시), DINNER(저녁 18시) 중 선택해야 합니다.
            - 예약 날짜는 내일부터 가능합니다.
            """
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "예약 편지 저장 성공",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(
                    name = "성공 응답",
                    value = ApiResponseConstants.SUCCESS_RESPONSE,
                    summary = "예약 성공"
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = {
                    @ExampleObject(
                        name = "잘못된 예약 시간",
                        value = "{\"isSuccess\": false, \"code\": \"LETTER_400_2\", \"message\": \"예약 가능한 시간은 MORNING(아침 9시), LUNCH(점심 13시), DINNER(저녁 18시)입니다.\"}",
                        summary = "잘못된 예약 시간"
                    ),
                    @ExampleObject(
                        name = "잘못된 전화번호 형식",
                        value = "{\"isSuccess\": false, \"code\": \"LETTER_400_6\", \"message\": \"전화번호는 010으로 시작하는 11자리 숫자여야 합니다.\"}",
                        summary = "잘못된 전화번호 형식"
                    )
                }
            )
        )
    })
    CommonResponse<Void> saveReservationLetter(
        @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails authentication,
        @RequestBody @Validated LetterRequestDTO.ReservationRequest request
    );

    @Operation(summary = "임시 저장", description = "편지를 임시 저장합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "임시 저장 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ApiResponseConstants.SUCCESS_RESPONSE)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ApiResponseConstants.BAD_REQUEST_RESPONSE)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ApiResponseConstants.SERVER_ERROR_RESPONSE)
                    )
            )
    })
    CommonResponse<Void> saveDraftLetter(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails authentication,
            @RequestBody LetterRequestDTO.DraftRequest request
    );

    @Operation(summary = "예약 편지 취소", description = "예약된 편지를 취소합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "예약 취소 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ApiResponseConstants.SUCCESS_RESPONSE)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "편지를 찾을 수 없음",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ApiResponseConstants.NOT_FOUND_RESPONSE)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ApiResponseConstants.SERVER_ERROR_RESPONSE)
                    )
            )
    })
    CommonResponse<Void> cancelReservationLetter(
            @Parameter(description = "취소할 편지 ID", required = true) @PathVariable Long letterId);

    @Operation(summary = "편지 종류별 개수 요약", description = "예약/발송/수신/임시저장 편지의 개수를 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = "{\"isSuccess\": true, \"code\": \"COMMON200\", \"message\": \"성공입니다.\", \"result\": {\"reservedCount\": 5, \"sentCount\": 10, \"receivedCount\": 3, \"draftCount\": 2}}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ApiResponseConstants.SERVER_ERROR_RESPONSE)
                    )
            )
    })
    CommonResponse<LetterResponseDTO.LetterSummaryResponse> getLetterSummary(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails authentication
    );

    @Operation(summary = "예약 편지 목록 조회", description = "예약된 편지 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = "{\"isSuccess\": true, \"code\": \"COMMON200\", \"message\": \"성공입니다.\", \"result\": {\"letters\": [{\"letterId\": 1, \"date\": \"2023-05-01\", \"time\": \"14:00\", \"recipientOrSender\": \"홍길동\", \"preview\": \"안녕하세요...\"}], \"isReceived\": false}}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ApiResponseConstants.SERVER_ERROR_RESPONSE)
                    )
            )
    })
    CommonResponse<LetterResponseDTO.LetterListResponse> getReservedLetters(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails authentication
    );

    @Operation(summary = "보낸 편지 목록 조회", description = "발송 완료된 편지 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = "{\"isSuccess\": true, \"code\": \"COMMON200\", \"message\": \"성공입니다.\", \"result\": {\"letters\": [{\"letterId\": 1, \"date\": \"2023-05-01\", \"time\": \"14:00\", \"recipientOrSender\": \"홍길동\", \"preview\": \"안녕하세요...\"}], \"isReceived\": false}}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ApiResponseConstants.SERVER_ERROR_RESPONSE)
                    )
            )
    })
    CommonResponse<LetterResponseDTO.LetterListResponse> getSentLetters(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails authentication
    );

    @Operation(summary = "받은 편지 목록 조회", description = "수신한 편지 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = "{\"isSuccess\": true, \"code\": \"COMMON200\", \"message\": \"성공입니다.\", \"result\": {\"letters\": [{\"letterId\": 1, \"date\": \"2023-05-01\", \"time\": \"14:00\", \"recipientOrSender\": \"김철수\", \"preview\": \"안녕하세요...\"}], \"isReceived\": true}}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ApiResponseConstants.SERVER_ERROR_RESPONSE)
                    )
            )
    })
    CommonResponse<LetterResponseDTO.LetterListResponse> getReceivedLetters(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails authentication
    );

    @Operation(summary = "예약 편지 상세 조회", description = "예약된 편지의 상세 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = "{\"isSuccess\": true, \"code\": \"COMMON200\", \"message\": \"성공입니다.\", \"result\": {\"reservationDate\": \"2023-05-01\", \"reservationTime\": \"14:00\", \"recipientOrSender\": \"홍길동\", \"daysUntilReservation\": 5, \"content\": \"안녕하세요, 반갑습니다...\"}}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "편지를 찾을 수 없음",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ApiResponseConstants.NOT_FOUND_RESPONSE)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ApiResponseConstants.SERVER_ERROR_RESPONSE)
                    )
            )
    })
    CommonResponse<LetterResponseDTO.LetterDetailResponse> getReservedLetter(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails authentication,
            @Parameter(description = "조회할 편지 ID", required = true) @PathVariable Long letterId
    );

    @Operation(summary = "보낸 편지 상세 조회", description = "발송 완료된 편지의 상세 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = "{\"isSuccess\": true, \"code\": \"COMMON200\", \"message\": \"성공입니다.\", \"result\": {\"reservationDate\": \"2023-05-01\", \"reservationTime\": \"14:00\", \"recipientOrSender\": \"홍길동\", \"content\": \"안녕하세요, 반갑습니다...\"}}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "편지를 찾을 수 없음",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ApiResponseConstants.NOT_FOUND_RESPONSE)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ApiResponseConstants.SERVER_ERROR_RESPONSE)
                    )
            )
    })
    CommonResponse<LetterResponseDTO.LetterDetailResponse> getSentLetter(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails authentication,
            @Parameter(description = "조회할 편지 ID", required = true) @PathVariable Long letterId
    );

    @Operation(summary = "받은 편지 상세 조회", description = "수신한 편지의 상세 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = "{\"isSuccess\": true, \"code\": \"COMMON200\", \"message\": \"성공입니다.\", \"result\": {\"reservationDate\": \"2023-05-01\", \"reservationTime\": \"14:00\", \"recipientOrSender\": \"김철수\", \"content\": \"안녕하세요, 반갑습니다...\"}}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "편지를 찾을 수 없음",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ApiResponseConstants.NOT_FOUND_RESPONSE)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ApiResponseConstants.SERVER_ERROR_RESPONSE)
                    )
            )
    })
    CommonResponse<LetterResponseDTO.LetterDetailResponse> getReceivedLetter(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails authentication,
            @Parameter(description = "조회할 편지 ID", required = true) @PathVariable Long letterId
    );

    @Operation(summary = "임시보관 편지 목록 조회", description = "임시보관된 편지 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = "{\"isSuccess\": true, \"code\": \"COMMON200\", \"message\": \"성공입니다.\", \"result\": {\"letters\": [{\"letterId\": 1, \"recipientOrSender\": \"홍길동\", \"preview\": \"안녕하세요...\"}], \"isReceived\": false}}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ApiResponseConstants.SERVER_ERROR_RESPONSE)
                    )
            )
    })
    CommonResponse<LetterResponseDTO.LetterListResponse> getDraftLetters(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails authentication
    );

    @Operation(summary = "임시보관 편지 상세 조회", description = "임시보관된 편지의 상세 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = "{\"isSuccess\": true, \"code\": \"COMMON200\", \"message\": \"성공입니다.\", \"result\": {\"recipientOrSender\": \"홍길동\", \"content\": \"안녕하세요, 반갑습니다...\"}}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "편지를 찾을 수 없음",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ApiResponseConstants.NOT_FOUND_RESPONSE)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ApiResponseConstants.SERVER_ERROR_RESPONSE)
                    )
            )
    })
    CommonResponse<LetterResponseDTO.LetterDetailResponse> getDraftLetter(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails authentication,
            @Parameter(description = "조회할 편지 ID", required = true) @PathVariable Long letterId
    );
}
