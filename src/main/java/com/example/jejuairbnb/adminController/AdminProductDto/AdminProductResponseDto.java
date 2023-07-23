package com.example.jejuairbnb.adminController.AdminProductDto;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class AdminProductResponseDto {
    private boolean ok;
    private HttpStatus statusCode;
    private String message;
}
