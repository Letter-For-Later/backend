package org.example.letter.domain.letter.service;

import lombok.RequiredArgsConstructor;
import org.example.letter.domain.letter.dto.LetterRequestDTO;
import org.example.letter.domain.letter.dto.LetterResponseDTO;
import org.example.letter.domain.letter.entity.Letter;
import org.example.letter.domain.letter.entity.LetterStatus;
import org.example.letter.domain.letter.repository.LetterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LetterService {

    private final LetterRepository letterRepository;

    @Transactional
    public void saveReservationLetter(LetterRequestDTO.ReservationRequest request) {
        Letter letter = request.toEntity();
        letterRepository.save(letter);
    }

    @Transactional
    public void saveDraftLetter(LetterRequestDTO.DraftRequest request) {
        Letter letter = request.toEntity();
        letterRepository.save(letter);
    }

    @Transactional
    public void cancelReservationLetter(Long letterId) {
        Letter letter = findLetterById(letterId);
        letterRepository.delete(letter);
    }

    public LetterResponseDTO.LetterSummaryResponse getLetterSummary() {
        return LetterResponseDTO.LetterSummaryResponse.builder()
                .reservedCount(letterRepository.countByStatus(LetterStatus.RESERVED))
                .sentCount(letterRepository.countByStatus(LetterStatus.SENT))
                .receivedCount(letterRepository.countByStatus(LetterStatus.READ))
                .draftCount(letterRepository.countByStatus(LetterStatus.DRAFT))
                .build();
    }

    public List<Letter> getReservedLetters() {
        return letterRepository.findAllByStatus(LetterStatus.RESERVED);
    }

    public List<Letter> getSentLetters() {
        return letterRepository.findAllByStatus(LetterStatus.SENT);
    }

    public List<Letter> getReceivedLetters() {
        return letterRepository.findAllByStatus(LetterStatus.READ);
    }

    public List<Letter> getDraftLetters() {
        return letterRepository.findAllByStatus(LetterStatus.DRAFT);
    }

    public Letter getLetter(Long letterId) {
        return findLetterById(letterId);
    }

    private Letter findLetterById(Long letterId) {
        return letterRepository.findById(letterId)
                .orElseThrow(() -> new IllegalArgumentException("편지를 찾을 수 없습니다."));
    }
}
