package org.example.letter.domain.letter.controller;

import lombok.RequiredArgsConstructor;
import org.example.letter.domain.auth.auth.custom.CustomUserDetails;
import org.example.letter.domain.letter.docs.LetterControllerDocs;
import org.example.letter.domain.letter.dto.LetterRequestDTO;
import org.example.letter.domain.letter.dto.LetterResponseDTO;
import org.example.letter.domain.letter.service.LetterService;
import org.example.letter.global.payload.CommonResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/letters")
@RequiredArgsConstructor
public class LetterController implements LetterControllerDocs {

    private final LetterService letterService;

    @PostMapping("/reservation")
    public CommonResponse<Void> saveReservationLetter(
            @AuthenticationPrincipal CustomUserDetails authentication,
            @RequestBody @Validated LetterRequestDTO.ReservationRequest request
    ) {
        letterService.saveReservationLetter(authentication.getUserId(), request);
        return CommonResponse.onSuccess(null);
    }

    @PostMapping("/draft")
    public CommonResponse<Void> saveDraftLetter(
            @AuthenticationPrincipal CustomUserDetails authentication,
            @RequestBody @Validated LetterRequestDTO.DraftRequest request
    ) {
        letterService.saveDraftLetter(authentication.getUserId(), request);
        return CommonResponse.onSuccess(null);
    }

    @DeleteMapping("/reservation/{letterId}")
    public CommonResponse<Void> cancelReservationLetter(@PathVariable Long letterId) {
        letterService.cancelReservationLetter(letterId);
        return CommonResponse.onSuccess(null);
    }

    @GetMapping("/summary")
    public CommonResponse<LetterResponseDTO.LetterSummaryResponse> getLetterSummary(
            @AuthenticationPrincipal CustomUserDetails authentication
    ) {
        return CommonResponse.onSuccess(letterService.getLetterSummary(authentication.getUserId()));
    }

    // 날짜, 시간, 수신자(내가 보내서 받는 사람)
    // 시간은 아침(9시), 점심(13시), 저녁(18시)
    @GetMapping("/reserved")
    public CommonResponse<LetterResponseDTO.LetterListResponse> getReservedLetters(
            @AuthenticationPrincipal CustomUserDetails authentication
    ) {
        return CommonResponse.onSuccess(
                LetterResponseDTO.LetterListResponse.of(
                        letterService.getReservedLetters(authentication.getUserId()), false));
    }

    // 날짜, 시간, 수신자(내가 보내서 받는 사람)
    // 시간은 아침(9시), 점심(13시), 저녁(18시)
    @GetMapping("/sent")
    public CommonResponse<LetterResponseDTO.LetterListResponse> getSentLetters(
            @AuthenticationPrincipal CustomUserDetails authentication
    ) {
        return CommonResponse.onSuccess(
                LetterResponseDTO.LetterListResponse.of(
                        letterService.getSentLetters(authentication.getUserId()), false));
    }

    // 날짜, 시간, 발신자(나에게 보낸 사람)
    // 시간은 아침(9시), 점심(13시), 저녁(18시)
    @GetMapping("/received")
    public CommonResponse<LetterResponseDTO.LetterListResponse> getReceivedLetters(
            @AuthenticationPrincipal CustomUserDetails authentication
    ) {
        return CommonResponse.onSuccess(
                LetterResponseDTO.LetterListResponse.of(
                        letterService.getReceivedLetters(authentication.getUserId()), true));
    }

    // letterId 와 이름을 리스트로 담아서 주자
    // 날짜 및 시간은 null 로 보내자
    @GetMapping("/drafts")
    public CommonResponse<LetterResponseDTO.LetterListResponse> getDraftLetters(
            @AuthenticationPrincipal CustomUserDetails authentication
    ) {
        return CommonResponse.onSuccess(
                LetterResponseDTO.LetterListResponse.of(
                        letterService.getDraftLetters(authentication.getUserId()), false));
    }

    // 날짜, 시간, 수신자(내가 보내서 받는 사람), 내용, 발송까지 남은 기간(day)
    @GetMapping("/reserved/{letterId}")
    public CommonResponse<LetterResponseDTO.LetterDetailResponse> getReservedLetter(
            @AuthenticationPrincipal CustomUserDetails authentication,
            @PathVariable Long letterId
    ) {
        return CommonResponse.onSuccess(
                LetterResponseDTO.LetterDetailResponse.fromSentOrReceived(
                        letterService.getLetter(authentication.getUserId(), letterId), false));
    }

    // 날짜, 시간, 수신자(내가 보내서 받는 사람), 내용
    @GetMapping("/sent/{letterId}")
    public CommonResponse<LetterResponseDTO.LetterDetailResponse> getSentLetter(
            @AuthenticationPrincipal CustomUserDetails authentication,
            @PathVariable Long letterId
    ) {
        return CommonResponse.onSuccess(
                LetterResponseDTO.LetterDetailResponse.fromSentOrReceived(
                        letterService.getLetter(authentication.getUserId(), letterId), false));
    }

    // 날짜, 시간, 발신자(나에게 보낸 사람), 내용
    @GetMapping("/received/{letterId}")
    public CommonResponse<LetterResponseDTO.LetterDetailResponse> getReceivedLetter(
            @AuthenticationPrincipal CustomUserDetails authentication,
            @PathVariable Long letterId
    ) {
        return CommonResponse.onSuccess(
                LetterResponseDTO.LetterDetailResponse.fromSentOrReceived(
                        letterService.getLetter(authentication.getUserId(), letterId), true));
    }

    // 발신자, 내용, 수신자
    @GetMapping("/drafts/{letterId}")
    public CommonResponse<LetterResponseDTO.LetterDetailResponse> getDraftLetter(
            @AuthenticationPrincipal CustomUserDetails authentication,
            @PathVariable Long letterId
    ) {
        return CommonResponse.onSuccess(
                LetterResponseDTO.LetterDetailResponse.fromSentOrReceived(
                        letterService.getLetter(authentication.getUserId(), letterId), false));
    }
}