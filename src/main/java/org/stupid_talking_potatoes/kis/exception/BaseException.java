package org.stupid_talking_potatoes.kis.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class BaseException extends RuntimeException {
    private String message;
    private String content;
}
