package com.example.jejuairbnb.adminController.AdminProductDto.UpdateProductDto;

import lombok.Data;

@Data
public class UpdateProductRequestDto {
    private Long id;
    private String name;
    private Long price;
    private String img;
}
