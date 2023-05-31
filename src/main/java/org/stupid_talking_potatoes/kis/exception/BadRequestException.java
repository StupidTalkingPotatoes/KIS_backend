package org.stupid_talking_potatoes.kis.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {
    private String message;
    private String content;

    @Builder
    public BadRequestException(String message, String content) {
        this.message = message;
        this.content = content;
    }
}
