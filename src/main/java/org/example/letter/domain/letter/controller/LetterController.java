package org.example.letter.domain.letter.controller;

import lombok.RequiredArgsConstructor;
import org.example.letter.domain.letter.docs.LetterControllerDocs;
import org.example.letter.domain.letter.dto.LetterRequestDTO;
import org.example.letter.domain.letter.dto.LetterResponseDTO;
import org.example.letter.domain.letter.service.LetterService;
import org.example.letter.global.payload.CommonResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/reserved")
    public CommonResponse<LetterResponseDTO.LetterListResponse> getReservedLetters() {
        return CommonResponse.onSuccess(
                LetterResponseDTO.LetterListResponse.of(
                        letterService.getReservedLetters(), false));
    }

    @GetMapping("/sent")
    public CommonResponse<LetterResponseDTO.LetterListResponse> getSentLetters() {
        return CommonResponse.onSuccess(
                LetterResponseDTO.LetterListResponse.of(
                        letterService.getSentLetters(), false));
    }

    @GetMapping("/received")
    public CommonResponse<LetterResponseDTO.LetterListResponse> getReceivedLetters() {
        return CommonResponse.onSuccess(
                LetterResponseDTO.LetterListResponse.of(
                        letterService.getReceivedLetters(), true));
    }

    @GetMapping("/reserved/{letterId}")
    public CommonResponse<LetterResponseDTO.LetterDetailResponse> getReservedLetter(
            @PathVariable Long letterId
    ) {
        return CommonResponse.onSuccess(
                LetterResponseDTO.LetterDetailResponse.fromReserved(
                        letterService.getLetter(letterId)));
    }

    @GetMapping("/sent/{letterId}")
    public CommonResponse<LetterResponseDTO.LetterDetailResponse> getSentLetter(
            @PathVariable Long letterId
    ) {
        return CommonResponse.onSuccess(
                LetterResponseDTO.LetterDetailResponse.fromSentOrReceived(
                        letterService.getLetter(letterId), false));
    }

    @GetMapping("/received/{letterId}")
    public CommonResponse<LetterResponseDTO.LetterDetailResponse> getReceivedLetter(
            @PathVariable Long letterId
    ) {
        return CommonResponse.onSuccess(
                LetterResponseDTO.LetterDetailResponse.fromSentOrReceived(
                        letterService.getLetter(letterId), true));
    }

    @GetMapping("/drafts")
    public CommonResponse<LetterResponseDTO.LetterListResponse> getDraftLetters() {
        return CommonResponse.onSuccess(
                LetterResponseDTO.LetterListResponse.of(
                        letterService.getDraftLetters(), false));
    }

    @GetMapping("/drafts/{letterId}")
    public CommonResponse<LetterResponseDTO.LetterDetailResponse> getDraftLetter(
            @PathVariable Long letterId
    ) {
        return CommonResponse.onSuccess(
                LetterResponseDTO.LetterDetailResponse.fromDraft(
                        letterService.getLetter(letterId)));
    }
}