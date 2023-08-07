package com.example.jejuairbnb.controller.ProductControllerDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class FindProductResponseDto {
    private List<ProductDto> products;
    private int size;
    private int totalPages;
}
