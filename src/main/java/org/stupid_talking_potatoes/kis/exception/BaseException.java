package org.stupid_talking_potatoes.kis.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class BaseException extends RuntimeException {
    private String message;
    private String content;
    private HttpStatus statusCode;

    public BaseException(String message, String content, HttpStatus statusCode) {
        // 문자열에 \"가 있는 경우 작은 따옴표로 변경
        this.message = message.replace("\"", "'");
        this.content = content.replace("\"", "'");
        this.statusCode = statusCode;
    }
}
