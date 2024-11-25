package com.oreo.finalproject_5re5_be.tts.exception;

import com.oreo.finalproject_5re5_be.global.exception.InValidValueException;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DuplicatedSentenceException extends InValidValueException {

    public DuplicatedSentenceException(String message) {
        super(message);
    }
}
