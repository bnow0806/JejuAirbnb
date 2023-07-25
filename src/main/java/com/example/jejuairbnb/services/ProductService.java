package com.example.jejuairbnb.services;

import com.example.jejuairbnb.controller.ProductControllerDto.FindProductOneResponseDto;
import com.example.jejuairbnb.controller.ProductControllerDto.FindProductResponseDto;
import com.example.jejuairbnb.domain.Product;
import com.example.jejuairbnb.repository.IProductRepository;
import com.example.jejuairbnb.shared.exception.HttpException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductService {
    private final IProductRepository productRepository;

    public FindProductOneResponseDto findProductOneById(
            Long id
    ) {
        Product findProduct = productRepository.findById(id)
                .orElseThrow(() -> new HttpException(
                        false,
                        "존재하지 않는 상품입니다.",
                        HttpStatus.NOT_FOUND
                ));
		return FindProductOneResponseDto
                .builder()
                .name(findProduct.getName())
                .description(findProduct.getDescription())
                .price(findProduct.getPrice())
                .img(findProduct.getImg())
                .build();
    }

    public FindProductResponseDto findProduct() {

        return FindProductResponseDto
                .builder()
                .products(productRepository.findAll())
                .build();
    }
}
