package org.stupid_talking_potatoes.kis.exception;

import lombok.Builder;
import org.springframework.http.HttpStatus;

/**
 * package :  org.stupid_talking_potatoes.kis.exception
 * fileName : InvalidUrlException
 * author :  ShinYeaChan
 * date : 2023-06-04
 */
public class InvalidUrlException extends BaseException{
    @Builder
    public InvalidUrlException(String message, String content) {
        super(message, content,HttpStatus.BAD_REQUEST);
    }
}
