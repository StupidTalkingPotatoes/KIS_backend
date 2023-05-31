package org.stupid_talking_potatoes.kis.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NotFoundException extends BaseException {
    @Builder
    public NotFoundException(String message, String content) {
        super(message, content, HttpStatus.NOT_FOUND);
    }
}
