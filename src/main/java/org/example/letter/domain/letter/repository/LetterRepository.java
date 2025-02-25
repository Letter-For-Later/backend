package org.example.letter.domain.letter.repository;

import org.example.letter.domain.letter.entity.Letter;
import org.example.letter.domain.letter.entity.LetterStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LetterRepository extends JpaRepository<Letter, Long> {
    int countByStatus(LetterStatus status);
    List<Letter> findAllByStatus(LetterStatus status);
} 