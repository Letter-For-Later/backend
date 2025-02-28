package org.example.letter.domain.letter.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.letter.global.payload.BaseErrorCode;
import org.example.letter.global.payload.ErrorReasonDTO;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum LetterErrorCode implements BaseErrorCode {
<<<<<<< Updated upstream
    LETTER_ALREADY_SENT(HttpStatus.BAD_REQUEST, "LETTER_400_l_1", "이미 발송된 편지입니다."),
    LETTER_INVALID_RESERVATION(HttpStatus.BAD_REQUEST, "LETTER_400_l_2", "잘못된 예약 시간입니다."),
    LETTER_CONTENT_TOO_LONG(HttpStatus.BAD_REQUEST, "LETTER_400_l_3", "편지 내용이 너무 깁니다."),
    LETTER_SENDER_REQUIRED(HttpStatus.BAD_REQUEST, "LETTER_400_l_4", "발신자 정보는 필수입니다."),
    LETTER_CONTENT_REQUIRED(HttpStatus.BAD_REQUEST, "LETTER_400_l_5", "편지 내용은 필수입니다."),
    LETTER_RECEIVER_REQUIRED(HttpStatus.BAD_REQUEST, "LETTER_400_l_6", "수신자 정보는 필수입니다."),
    LETTER_RECEIVER_TOO_LONG(HttpStatus.BAD_REQUEST, "LETTER_400_l_7", "수신자 이름이 너무 깁니다."),
    LETTER_INVALID_STATUS(HttpStatus.BAD_REQUEST, "LETTER_400_l_8", "잘못된 편지 상태입니다."),
    LETTER_INVALID_OPERATION(HttpStatus.BAD_REQUEST, "LETTER_400_l_9", "현재 상태에서 수행할 수 없는 작업입니다."),
    LETTER_PHONE_NUMBER_REQUIRED(HttpStatus.BAD_REQUEST, "LETTER_400_l_10", "전화번호는 필수입니다."),
    LETTER_INVALID_PHONE_NUMBER(HttpStatus.BAD_REQUEST, "LETTER_400_l_11", "유효하지 않은 전화번호 형식입니다."),
    LETTER_RESERVATION_DATE_REQUIRED(HttpStatus.BAD_REQUEST, "LETTER_400_l_12", "예약 날짜는 필수입니다."),
    LETTER_RESERVATION_TIME_REQUIRED(HttpStatus.BAD_REQUEST, "LETTER_400_l_13", "예약 시간은 필수입니다."),
    LETTER_SENDER_TOO_LONG(HttpStatus.BAD_REQUEST, "LETTER_400_l_14", "발신자 이름이 너무 깁니다."),

    LETTER_NOT_FOUND(HttpStatus.NOT_FOUND, "LETTER_404_l_1", "존재하지 않는 편지입니다."),
    LETTER_DRAFT_NOT_FOUND(HttpStatus.NOT_FOUND, "LETTER_404_l_2", "존재하지 않는 임시 저장 편지입니다.");
=======
    LETTER_NOT_FOUND(HttpStatus.NOT_FOUND, "LETTER_404", "존재하지 않는 편지입니다."),
    LETTER_ALREADY_SENT(HttpStatus.BAD_REQUEST, "LETTER_400_1", "이미 발송된 편지입니다."),
    LETTER_INVALID_RESERVATION(HttpStatus.BAD_REQUEST, "LETTER_400_2", "잘못된 예약 시간입니다."),
    LETTER_CONTENT_TOO_LONG(HttpStatus.BAD_REQUEST, "LETTER_400_3", "편지 내용이 너무 깁니다."),
    LETTER_SENDER_REQUIRED(HttpStatus.BAD_REQUEST, "LETTER_400_4", "발신자 정보는 필수입니다."),
    LETTER_CONTENT_REQUIRED(HttpStatus.BAD_REQUEST, "LETTER_400_5", "편지 내용은 필수입니다."),
    LETTER_RECEIVER_REQUIRED(HttpStatus.BAD_REQUEST, "LETTER_400_6", "수신자 정보는 필수입니다."),
    LETTER_RECEIVER_TOO_LONG(HttpStatus.BAD_REQUEST, "LETTER_400_7", "수신자 이름이 너무 깁니다."),
    LETTER_INVALID_STATUS(HttpStatus.BAD_REQUEST, "LETTER_400_8", "잘못된 편지 상태입니다."),
    LETTER_INVALID_OPERATION(HttpStatus.BAD_REQUEST, "LETTER_400_9", "현재 상태에서 수행할 수 없는 작업입니다."),
    LETTER_PHONE_NUMBER_REQUIRED(HttpStatus.BAD_REQUEST, "LETTER_400_10", "전화번호는 필수입니다."),
    LETTER_INVALID_PHONE_NUMBER(HttpStatus.BAD_REQUEST, "LETTER_400_11", "유효하지 않은 전화번호 형식입니다."),
    LETTER_RESERVATION_DATE_REQUIRED(HttpStatus.BAD_REQUEST, "LETTER_400_12", "예약 날짜는 필수입니다."),
    LETTER_RESERVATION_TIME_REQUIRED(HttpStatus.BAD_REQUEST, "LETTER_400_13", "예약 시간은 필수입니다."),
    LETTER_ALREADY_READ(HttpStatus.BAD_REQUEST, "LETTER_400_14", "이미 읽은 편지입니다."),
    LETTER_DRAFT_NOT_FOUND(HttpStatus.NOT_FOUND, "LETTER_404_1", "존재하지 않는 임시 저장 편지입니다.");
>>>>>>> Stashed changes

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
