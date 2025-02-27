package org.example.letter.domain.letter.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.letter.global.payload.BaseErrorCode;
import org.example.letter.global.payload.ErrorReasonDTO;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum LetterErrorCode implements BaseErrorCode {
    LETTER_NOT_FOUND(HttpStatus.NOT_FOUND, "LETTER_404", "존재하지 않는 편지입니다."),
    LETTER_ALREADY_SENT(HttpStatus.BAD_REQUEST, "LETTER_400_1", "이미 발송된 편지입니다."),
    LETTER_INVALID_RESERVATION(HttpStatus.BAD_REQUEST, "LETTER_400_2", "잘못된 예약 시간입니다."),
    LETTER_CONTENT_TOO_LONG(HttpStatus.BAD_REQUEST, "LETTER_400_3", "편지 내용이 너무 깁니다."),
    LETTER_SENDER_REQUIRED(HttpStatus.BAD_REQUEST, "LETTER_400_4", "발신자 정보는 필수입니다."),
    LETTER_INVALID_DATE(HttpStatus.BAD_REQUEST, "LETTER_400_5", "유효하지 않은 날짜입니다."),
    LETTER_INVALID_PHONE_FORMAT(HttpStatus.BAD_REQUEST, "LETTER_400_6", "전화번호는 010으로 시작하는 11자리 숫자여야 합니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}
