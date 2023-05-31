package org.stupid_talking_potatoes.kis.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BadRequestException extends BaseException {

    @Builder
    public BadRequestException(String message, String content) {
        super(message, content);
    }
}
