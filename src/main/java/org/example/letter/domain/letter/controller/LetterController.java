package org.example.letter.domain.letter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.letter.domain.letter.dto.LetterRequestDTO;
import org.example.letter.domain.letter.dto.LetterResponseDTO;
import org.example.letter.domain.letter.service.LetterService;
import org.example.letter.global.payload.CommonResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "편지 API") // 너무 길어서 스웨거 부분은 인터페이스로 리팩토링 필요
@RestController
@RequestMapping("/api/letters")
@RequiredArgsConstructor
public class LetterController {

    private final LetterService letterService;

    @Operation(summary = "예약 편지 저장", description = "새로운 예약 편지를 저장합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "예약 편지 저장 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/reservation")
    public CommonResponse<Void> saveReservationLetter(
            @RequestBody @Validated LetterRequestDTO.ReservationRequest request
    ) {
        letterService.saveReservationLetter(request);
        return CommonResponse.onSuccess(null);
    }

    @Operation(summary = "임시 저장", description = "편지를 임시 저장합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "임시 저장 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/draft")
    public CommonResponse<Void> saveDraftLetter(
            @RequestBody @Validated LetterRequestDTO.DraftRequest request
    ) {
        letterService.saveDraftLetter(request);
        return CommonResponse.onSuccess(null);
    }

    @Operation(summary = "예약 편지 취소", description = "예약된 편지를 취소합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "예약 취소 성공"),
            @ApiResponse(responseCode = "404", description = "편지를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @DeleteMapping("/reservation/{letterId}")
    public CommonResponse<Void> cancelReservationLetter(
            @Parameter(description = "취소할 편지 ID", required = true)
            @PathVariable Long letterId
    ) {
        letterService.cancelReservationLetter(letterId);
        return CommonResponse.onSuccess(null);
    }

    @Operation(summary = "편지 종류별 개수 요약", description = "예약/발송/수신/임시저장 편지의 개수를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/summary")
    public CommonResponse<LetterResponseDTO.LetterSummaryResponse> getLetterSummary() {
        return CommonResponse.onSuccess(letterService.getLetterSummary());
    }

    @Operation(summary = "예약 편지 목록 조회", description = "예약된 편지 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/reserved")
    public CommonResponse<LetterResponseDTO.LetterListResponse> getReservedLetters() {
        return CommonResponse.onSuccess(
                LetterResponseDTO.LetterListResponse.of(
                        letterService.getReservedLetters(), false));
    }

    @Operation(summary = "보낸 편지 목록 조회", description = "발송 완료된 편지 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/sent")
    public CommonResponse<LetterResponseDTO.LetterListResponse> getSentLetters() {
        return CommonResponse.onSuccess(
                LetterResponseDTO.LetterListResponse.of(
                        letterService.getSentLetters(), false));
    }

    @Operation(summary = "받은 편지 목록 조회", description = "수신한 편지 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/received")
    public CommonResponse<LetterResponseDTO.LetterListResponse> getReceivedLetters() {
        return CommonResponse.onSuccess(
                LetterResponseDTO.LetterListResponse.of(
                        letterService.getReceivedLetters(), true));
    }

    @Operation(summary = "예약 편지 상세 조회", description = "예약된 편지의 상세 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "편지를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/reserved/{letterId}")
    public CommonResponse<LetterResponseDTO.LetterDetailResponse> getReservedLetter(
            @Parameter(description = "조회할 편지 ID", required = true)
            @PathVariable Long letterId
    ) {
        return CommonResponse.onSuccess(
                LetterResponseDTO.LetterDetailResponse.fromReserved(
                        letterService.getLetter(letterId)));
    }

    @Operation(summary = "보낸 편지 상세 조회", description = "발송 완료된 편지의 상세 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "편지를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/sent/{letterId}")
    public CommonResponse<LetterResponseDTO.LetterDetailResponse> getSentLetter(
            @Parameter(description = "조회할 편지 ID", required = true)
            @PathVariable Long letterId
    ) {
        return CommonResponse.onSuccess(
                LetterResponseDTO.LetterDetailResponse.fromSentOrReceived(
                        letterService.getLetter(letterId), false));
    }

    @Operation(summary = "받은 편지 상세 조회", description = "수신한 편지의 상세 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "편지를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/received/{letterId}")
    public CommonResponse<LetterResponseDTO.LetterDetailResponse> getReceivedLetter(
            @Parameter(description = "조회할 편지 ID", required = true)
            @PathVariable Long letterId
    ) {
        return CommonResponse.onSuccess(
                LetterResponseDTO.LetterDetailResponse.fromSentOrReceived(
                        letterService.getLetter(letterId), true));
    }

    @Operation(summary = "임시보관 편지 목록 조회", description = "임시보관된 편지 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/drafts")
    public CommonResponse<LetterResponseDTO.LetterListResponse> getDraftLetters() {
        return CommonResponse.onSuccess(
                LetterResponseDTO.LetterListResponse.of(
                        letterService.getDraftLetters(), false));
    }

    @Operation(summary = "임시보관 편지 상세 조회", description = "임시보관된 편지의 상세 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "편지를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/drafts/{letterId}")
    public CommonResponse<LetterResponseDTO.LetterDetailResponse> getDraftLetter(
            @Parameter(description = "조회할 편지 ID", required = true)
            @PathVariable Long letterId
    ) {
        return CommonResponse.onSuccess(
                LetterResponseDTO.LetterDetailResponse.fromDraft(
                        letterService.getLetter(letterId)));
    }
}