package org.stupid_talking_potatoes.kis.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private String message;
    private String content;

    public ErrorResponse(String message, String content) {
        this.message = message;
        this.content = content;
    }
}
