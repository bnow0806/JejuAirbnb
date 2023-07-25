package com.example.jejuairbnb.controller.ProductControllerDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FindProductOneResponseDto {
    private String name;
    private String description;
    private String content;
    private String img;
    private int price;
}
