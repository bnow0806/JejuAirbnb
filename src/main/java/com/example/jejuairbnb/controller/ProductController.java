package com.example.jejuairbnb.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "product", description = "상품 API")
@AllArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
}
