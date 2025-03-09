package org.example.letter.domain.letter.repository;

import org.example.letter.domain.letter.entity.Letter;
import org.example.letter.domain.letter.entity.LetterStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LetterRepository extends JpaRepository<Letter, Long> {
    int countByStatusAndUserUid(LetterStatus status, UUID userId);
    List<Letter> findAllByStatusAndUserUid(LetterStatus status, UUID userId);
    Optional<Letter> findByIdAndUserUid(Long letterId, UUID userId);
    
    // Letter와 Notification을 조인하여 조회하는 메서드 추가
    @Query("SELECT l FROM Letter l JOIN l.notification n " +
           "WHERE l.id = :letterId AND n.id = :notificationId")
    Optional<Letter> findByIdAndNotificationId(
        @Param("letterId") Long letterId, 
        @Param("notificationId") String notificationId
    );
} 