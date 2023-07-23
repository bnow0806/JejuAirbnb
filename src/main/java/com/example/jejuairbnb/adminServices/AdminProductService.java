package com.example.jejuairbnb.adminServices;

import com.example.jejuairbnb.adminController.AdminProductDto.CreateProductDto.CreateProductRequestDto;
import com.example.jejuairbnb.adminController.AdminProductDto.UpdateProductDto.UpdateProductRequestDto;
import com.example.jejuairbnb.domain.Product;
import com.example.jejuairbnb.domain.User;
import com.example.jejuairbnb.repository.IProductRepository;
import com.example.jejuairbnb.shared.Enum.ProviderEnum;
import com.example.jejuairbnb.shared.exception.HttpException;
import com.example.jejuairbnb.shared.response.CoreSuccessResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AdminProductService {
    private final IProductRepository productRepository;

    @Transactional
    public CoreSuccessResponse createProduct(
            User user,
            CreateProductRequestDto requestDto
    ) {
        if (user.getProvider() == ProviderEnum.FALSE){
            throw new HttpException(
                    false,
                    "PROVIDER 가 아닙니다.",
                    HttpStatus.NOT_FOUND
            );
        }

        Product createProductRequestDto = Product
                .builder()
                .name(requestDto.getName())
                .price(requestDto.getPrice())
                .img(requestDto.getImg())
                .build();

        productRepository.save(createProductRequestDto);
        return new CoreSuccessResponse(
                true,
                "상품이 등록되었습니다.",
                201
        );
    }

    @Transactional
    public CoreSuccessResponse updateProduct(
        Long id,
        User user,
        UpdateProductRequestDto updateProductRequestDto
    ) {
        Product findProduct = productRepository.findById(id).orElseThrow(
                () -> new HttpException(
                        false,
                        "해당 상품이 없습니다.",
                        HttpStatus.NOT_FOUND
                )
        );

        if (user.getProvider() == ProviderEnum.FALSE){
            throw new HttpException(
                    false,
                    "PROVIDER 가 아닙니다.",
                    HttpStatus.NOT_FOUND
            );
        }

        findProduct.setName(updateProductRequestDto.getName());
        findProduct.setPrice(updateProductRequestDto.getPrice());
        findProduct.setImg(updateProductRequestDto.getImg());
        productRepository.save(findProduct);

        return new CoreSuccessResponse(
                true,
                "상품이 수정되었습니다.",
                200
        );
    }

    @Transactional
    public CoreSuccessResponse deleteProduct(
            Long id,
            User user
    ) {
        Product findProduct = productRepository.findById(id)
                .orElseThrow(
                        () -> new HttpException(
                                false,
                                "해당 상품이 없습니다.",
                                HttpStatus.NOT_FOUND
                        )
                );

        if (user.getProvider() == ProviderEnum.FALSE) {
            throw new HttpException(
                    false,
                    "PROVIDER 가 아닙니다.",
                    HttpStatus.NOT_FOUND
            );
        }

        if (findProduct.getUserId() != user.getId()) {
            throw new HttpException(
                    false,
                    "해당 상품의 주인이 아닙니다.",
                    HttpStatus.NOT_FOUND
            );
        }

        productRepository.delete(findProduct);
        return new CoreSuccessResponse(
                true,
                "상품이 삭제되었습니다.",
                200
        );
    }
}
