package org.stupid_talking_potatoes.kis.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
    private String message;
    private String content;
    public NotFoundException(String message, String content) {
        this.message = message;
        this.content = content;
    }
}
