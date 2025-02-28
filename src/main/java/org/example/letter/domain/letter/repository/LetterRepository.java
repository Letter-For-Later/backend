package org.example.letter.domain.letter.repository;

import org.example.letter.domain.letter.entity.Letter;
import org.example.letter.domain.letter.entity.LetterStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LetterRepository extends JpaRepository<Letter, Long> {
    int countByStatusAndUserUid(LetterStatus status, UUID userId);
    List<Letter> findAllByStatusAndUserUid(LetterStatus status, UUID userId);
    Optional<Letter> findByIdAndUserUid(Long letterId, UUID userId);
} 