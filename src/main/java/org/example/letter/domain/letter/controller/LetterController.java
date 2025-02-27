package org.example.letter.domain.letter.controller;

import lombok.RequiredArgsConstructor;
import org.example.letter.domain.letter.docs.LetterControllerDocs;
import org.example.letter.domain.letter.dto.LetterRequestDTO;
import org.example.letter.domain.letter.dto.LetterResponseDTO;
import org.example.letter.domain.letter.service.LetterService;
import org.example.letter.global.payload.CommonResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/letters")
@RequiredArgsConstructor
public class LetterController implements LetterControllerDocs {

    private final LetterService letterService;

    @PostMapping("/reservation")
    public CommonResponse<Void> saveReservationLetter(
            @RequestBody @Validated LetterRequestDTO.ReservationRequest request
    ) {
        letterService.saveReservationLetter(request);
        return CommonResponse.onSuccess(null);
    }

    @PostMapping("/draft")
    public CommonResponse<Void> saveDraftLetter(
            @RequestBody @Validated LetterRequestDTO.DraftRequest request
    ) {
        letterService.saveDraftLetter(request);
        return CommonResponse.onSuccess(null);
    }

    @DeleteMapping("/reservation/{letterId}")
    public CommonResponse<Void> cancelReservationLetter(
            @PathVariable Long letterId
    ) {
        letterService.cancelReservationLetter(letterId);
        return CommonResponse.onSuccess(null);
    }

    @GetMapping("/summary")
    public CommonResponse<LetterResponseDTO.LetterSummaryResponse> getLetterSummary() {
        return CommonResponse.onSuccess(letterService.getLetterSummary());
    }

    // 날짜, 시간, 수신자(내가 보내서 받는 사람)
    // 시간은 아침(9시), 점심(13시), 저녁(18시)
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

    // 날짜, 시간, 수신자(내가 보내서 받는 사람)
    // 시간은 아침(9시), 점심(13시), 저녁(18시)
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

    // 날짜, 시간, 발신자(나에게 보낸 사람)
    // 시간은 아침(9시), 점심(13시), 저녁(18시)
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

    // letterId 와 이름을 리스트로 담아서 주자
    // 날짜 및 시간은 null 로 보내자
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

    // 날짜, 시간, 수신자(내가 보내서 받는 사람), 내용, 발송까지 남은 기간(day)
    @Operation(summary = "예약 편지 상세 조회", description = "예약된 편지의 상세 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "편지를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/reserved/{letterId}")
    public CommonResponse<LetterResponseDTO.LetterDetailResponse> getReservedLetter(
            @PathVariable Long letterId
    ) {
        return CommonResponse.onSuccess(
                LetterResponseDTO.LetterDetailResponse.fromReserved(
                        letterService.getLetter(letterId)));
    }

    // 날짜, 시간, 수신자(내가 보내서 받는 사람), 내용
    @Operation(summary = "보낸 편지 상세 조회", description = "발송 완료된 편지의 상세 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "편지를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/sent/{letterId}")
    public CommonResponse<LetterResponseDTO.LetterDetailResponse> getSentLetter(
            @PathVariable Long letterId
    ) {
        return CommonResponse.onSuccess(
                LetterResponseDTO.LetterDetailResponse.fromSentOrReceived(
                        letterService.getLetter(letterId), false));
    }

    // 날짜, 시간, 발신자(나에게 보낸 사람), 내용
    @Operation(summary = "받은 편지 상세 조회", description = "수신한 편지의 상세 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "편지를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/received/{letterId}")
    public CommonResponse<LetterResponseDTO.LetterDetailResponse> getReceivedLetter(
            @PathVariable Long letterId
    ) {
        return CommonResponse.onSuccess(
                LetterResponseDTO.LetterDetailResponse.fromSentOrReceived(
                        letterService.getLetter(letterId), true));
    }

    // 발신자, 내용, 수신자
    @Operation(summary = "임시보관 편지 상세 조회", description = "임시보관된 편지의 상세 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "편지를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/drafts/{letterId}")
    public CommonResponse<LetterResponseDTO.LetterDetailResponse> getDraftLetter(
            @PathVariable Long letterId
    ) {
        return CommonResponse.onSuccess(
                LetterResponseDTO.LetterDetailResponse.fromDraft(
                        letterService.getLetter(letterId)));
    }
}