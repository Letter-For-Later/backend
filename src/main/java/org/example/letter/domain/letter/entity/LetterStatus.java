package org.example.letter.domain.letter.entity;

public enum LetterStatus {
    DRAFT,          // 임시 저장
    RESERVED,       // 예약 완료
    SENT,           // 전송 완료
    READ            // 읽음
}