package org.stupid_talking_potatoes.kis.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * package :  org.stupid_talking_potatoes.kis.exception
 * fileName : NoContentException
 * author :  ShinYeaChan
 * date : 2023-06-04
 */@Getter
public class NoContentException extends BaseException {
    @Builder
    public NoContentException(String message, String content) {
        super(message, content, HttpStatus.NO_CONTENT);
    }
}
