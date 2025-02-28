package org.example.letter.domain.letter.service;

import lombok.RequiredArgsConstructor;
import org.example.letter.domain.letter.dto.LetterRequestDTO;
import org.example.letter.domain.letter.dto.LetterResponseDTO;
import org.example.letter.domain.letter.entity.Letter;
import org.example.letter.domain.letter.entity.LetterStatus;
import org.example.letter.domain.letter.exception.LetterErrorCode;
import org.example.letter.domain.letter.exception.LetterException;
import org.example.letter.domain.letter.repository.LetterRepository;
import org.example.letter.domain.user.entity.User;
import org.example.letter.domain.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LetterService { // 서비스 인터페이스 추가하기

    private final LetterRepository letterRepository;
    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(LetterService.class);

    @Transactional
    public void saveReservationLetter(UUID userId, LetterRequestDTO.ReservationRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Letter letter = request.toEntity(user);
        letterRepository.save(letter);
    }

    @Transactional
    public void saveDraftLetter(UUID userId, LetterRequestDTO.DraftRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Letter letter = request.toEntity(user);
        letterRepository.save(letter);
    }

    @Transactional
    public void cancelReservationLetter(Long letterId) {
        Letter letter = letterRepository.findById(letterId)
                .orElseThrow(() -> new LetterException(LetterErrorCode.LETTER_NOT_FOUND));
        
        letter.validateCancel();
        letterRepository.delete(letter);
    }

    public LetterResponseDTO.LetterSummaryResponse getLetterSummary(UUID userId) {
        return LetterResponseDTO.LetterSummaryResponse.builder()
                .reservedCount(letterRepository.countByStatusAndUserUid(LetterStatus.RESERVED, userId))
                .sentCount(letterRepository.countByStatusAndUserUid(LetterStatus.SENT, userId))
                .receivedCount(letterRepository.countByStatusAndUserUid(LetterStatus.READ, userId))
                .draftCount(letterRepository.countByStatusAndUserUid(LetterStatus.DRAFT, userId))
                .build();
    }

    public List<Letter> getReservedLetters(UUID userId) {
        return letterRepository.findAllByStatusAndUserUid(LetterStatus.RESERVED, userId);
    }

    public List<Letter> getSentLetters(UUID userId) {
        return letterRepository.findAllByStatusAndUserUid(LetterStatus.SENT, userId);
    }

    public List<Letter> getReceivedLetters(UUID userId) {
        return letterRepository.findAllByStatusAndUserUid(LetterStatus.READ, userId);
    }

    public List<Letter> getDraftLetters(UUID userId) {
        return letterRepository.findAllByStatusAndUserUid(LetterStatus.DRAFT, userId);
    }

    public Letter getLetter(UUID userId, Long letterId) {
        return letterRepository.findByIdAndUserUid(letterId, userId)
                .orElseThrow(() -> new LetterException(LetterErrorCode.LETTER_NOT_FOUND));
    }
}
