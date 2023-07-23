package com.example.jejuairbnb.shared.exception;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@Data
public class HttpException extends RuntimeException {
    private boolean ok;
    private String message;
    private HttpStatus httpStatus;

    public HttpException(
            boolean ok,
            String message,
            HttpStatus httpStatus
    ) {
        this.ok = ok;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
