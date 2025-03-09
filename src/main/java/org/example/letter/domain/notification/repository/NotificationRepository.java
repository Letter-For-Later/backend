package org.example.letter.domain.notification.repository;

import org.example.letter.domain.notification.entity.Notification;
import org.example.letter.domain.notification.entity.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {
    // 발송 대상 조회
    @Query("""
        SELECT n FROM Notification n 
        WHERE n.status = :status 
        AND n.reservationDateTime >= :startDateTime 
        AND n.reservationDateTime < :endDateTime 
        ORDER BY n.reservationDateTime ASC
    """)
    List<Notification> findAllByStatusAndReservationDateTimeRange(
        @Param("status") NotificationStatus status,
        @Param("startDateTime") LocalDateTime startDateTime,
        @Param("endDateTime") LocalDateTime endDateTime
    );

    // 재시도 대상 조회 추가
    @Query("SELECT n FROM Notification n " +
           "WHERE n.status = :status " +
           "AND n.retryCount < :maxRetryCount " +
           "AND n.reservationDateTime <= :dateTime")
    List<Notification> findAllForRetry(
            @Param("status") NotificationStatus status,
            @Param("maxRetryCount") int maxRetryCount,
            @Param("dateTime") LocalDateTime dateTime
    );
}
