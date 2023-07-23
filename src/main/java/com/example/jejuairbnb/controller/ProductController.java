package com.example.jejuairbnb.controller;

import com.example.jejuairbnb.controller.ProductControllerDto.FindProductDto.FindProductOneResponseDto;
import com.example.jejuairbnb.controller.ProductControllerDto.FindProductDto.FindProductResponseDto;
import com.example.jejuairbnb.services.ProductService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "product", description = "상품 API")
@AllArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping("/{id}")
    public FindProductOneResponseDto findProductOne(
            @Parameter(description = "상품 id", required = true) Long id
    ) {
        System.out.println("상품 조회 요청: " + id);
        return productService.findProductOneById(id);
    }

    @GetMapping()
    public FindProductResponseDto findProductAll() {
        return productService.findProduct();
    }
}
