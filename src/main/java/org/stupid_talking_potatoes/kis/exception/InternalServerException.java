package org.stupid_talking_potatoes.kis.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InternalServerException extends BaseException {
    @Builder
    public InternalServerException(String message, String content) {
        super(message, content, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
