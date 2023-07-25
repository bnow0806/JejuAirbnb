package com.example.jejuairbnb.shared.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(HttpException.class)
    public ResponseEntity<ExceptionResponse> handleHttpException(HttpException e) {
        HttpStatus httpStatus = e.getHttpStatus();
        String message = e.getMessage();
        return ResponseEntity.status(e.getHttpStatus()).body(
                new ExceptionResponse(
                        message,
                        httpStatus.value()
                ));
    }
}
