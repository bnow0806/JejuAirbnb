package com.example.jejuairbnb.services;

import com.example.jejuairbnb.controller.ProductControllerDto.FindProductDto.FindProductOneResponseDto;
import com.example.jejuairbnb.controller.ProductControllerDto.FindProductDto.FindProductResponseDto;
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
        System.out.println("상품 조회 요청: " + id);
        Product findProduct = productRepository.findById(id)
                .orElseThrow(() -> new HttpException("존재하지 않는 상품입니다.", HttpStatus.NOT_FOUND));

        FindProductOneResponseDto findProductOneResponseDto = new FindProductOneResponseDto();
        findProductOneResponseDto.setName(findProduct.getName());
        findProductOneResponseDto.setImg(findProduct.getImg());
        findProductOneResponseDto.setPrice(findProduct.getPrice());

        return findProductOneResponseDto;
    }

    public FindProductResponseDto findProduct() {
        System.out.println("상품 목록 조회 요청");
        FindProductResponseDto findProductResponseDto = new FindProductResponseDto();
        findProductResponseDto.setProducts(productRepository.findAll());

        return findProductResponseDto;
    }
}
