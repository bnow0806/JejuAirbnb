package com.example.jejuairbnb.controller.ProductControllerDto;

import com.example.jejuairbnb.domain.Product;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FindProductResponseDto {
    List<Product> products;
}
