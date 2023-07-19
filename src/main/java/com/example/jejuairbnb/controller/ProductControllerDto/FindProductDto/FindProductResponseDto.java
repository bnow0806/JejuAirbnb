package com.example.jejuairbnb.controller.ProductControllerDto.FindProductDto;

import com.example.jejuairbnb.domain.Product;
import lombok.Data;

import java.util.List;

@Data
public class FindProductResponseDto {
    List<Product> products;
}
