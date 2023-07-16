package com.example.jejuairbnb.shared.exception;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@Data
public class HttpException extends RuntimeException {
    private String message;
    private HttpStatus httpStatus;

    public HttpException(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
