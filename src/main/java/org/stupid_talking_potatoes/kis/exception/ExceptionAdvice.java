package org.stupid_talking_potatoes.kis.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler({BadRequestException.class, NotFoundException.class})
    public ResponseEntity<ErrorResponse> baseExceptionHandler(BaseException e) {
        ErrorResponse response = new ErrorResponse(e.getMessage(), e.getContent());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }
}
