package com.example.jejuairbnb.controller.ProductControllerDto;

import com.example.jejuairbnb.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class FindProductResponseDto {
    private List<Product> products;
    private int size;
    private int totalPages;
}
