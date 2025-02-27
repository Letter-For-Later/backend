package org.example.letter.domain.letter.exception;

import org.example.letter.global.exception.GeneralException;

public class LetterException extends GeneralException {
    
    public LetterException(LetterErrorCode errorCode) {
        super(errorCode);
    }
}
