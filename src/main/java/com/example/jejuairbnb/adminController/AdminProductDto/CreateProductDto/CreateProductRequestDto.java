package com.example.jejuairbnb.adminController.AdminProductDto.CreateProductDto;

import lombok.Data;

@Data
public class CreateProductRequestDto {
    private String name;
    private Long price;
    private String img;
    private int userId;
}
