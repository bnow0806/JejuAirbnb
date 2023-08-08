package com.example.jejuairbnb.adminController.AdminProductDto.CreateProductDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CreateProductRequestDto {
    @Schema(name = "name")
    private String name;
    @Schema(name = "description")
    private String description;
    @Schema(name = "price")
    private int price;
    @Schema(name = "img")
    private String img;
    @Schema(name = "userId")
    private Long userId;
}
