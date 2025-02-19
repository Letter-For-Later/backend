package org.example.letter.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.letter.global.payload.BaseErrorCode;
import org.example.letter.global.payload.ErrorReasonDTO;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

    private BaseErrorCode code;

    public ErrorReasonDTO getErrorReason() {
        return this.code.getReason();
    }

    public ErrorReasonDTO getErrorReasonHttpStatus() {
        return this.code.getReasonHttpStatus();
    }
}
