package com.example.jejuairbnb.services;

import com.example.jejuairbnb.controller.ProductControllerDto.FindProductOneResponseDto;
import com.example.jejuairbnb.controller.ProductControllerDto.FindProductResponseDto;
import com.example.jejuairbnb.controller.ProductControllerDto.ProductDto;
import com.example.jejuairbnb.domain.Comment;
import com.example.jejuairbnb.domain.Product;
import com.example.jejuairbnb.domain.User;
import com.example.jejuairbnb.repository.ICommentRepository;
import com.example.jejuairbnb.repository.IProductRepository;
import com.example.jejuairbnb.shared.Enum.ProviderEnum;
import com.example.jejuairbnb.shared.exception.HttpException;
import com.example.jejuairbnb.shared.response.CoreSuccessResponseWithData;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductService {
    private final IProductRepository productRepository;
    private final ICommentRepository commentRepository;

    public FindProductOneResponseDto findProductOneById(
            Long id
    ) {
        Product findProduct = productRepository.findById(id)
                .orElseThrow(() -> new HttpException(
                        false,
                        "존재하지 않는 상품입니다.",
                        HttpStatus.NOT_FOUND
                ));
        Long commentCount = commentRepository.countByProductId(id);
        Double commentAvg = commentRepository.avgByProductId(id);

        findProduct.setCommentAvg(commentAvg);
        findProduct.setCommentCount(commentCount);

        List<Comment> comments = commentRepository.findByProductId(id);
        List<Comment> commentDtos = toDtoList(comments);

		return FindProductOneResponseDto
                .builder()
                .name(findProduct.getName())
                .description(findProduct.getDescription())
                .price(findProduct.getPrice())
                .img(findProduct.getImg())
                .comments(commentDtos)
                .build();
    }

    public CoreSuccessResponseWithData findProduct(
            Pageable pageable
    ) {
        try {
            Page<Product> productPage = productRepository.findAll(pageable);
            List<Product> products = productPage.getContent();

            FindProductResponseDto findProductResponseDto = new FindProductResponseDto(
                    productDtos(products),
                    productPage.getContent().size(),
                    productPage.getTotalPages()
            );

            return new CoreSuccessResponseWithData(
                    true,
                    "상품 조회에 성공했습니다.",
                    200,
                    findProductResponseDto
            );
        } catch (Exception e) {
            throw new HttpException(
                    false,
                    "에러가 발생했습니다.",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    public FindProductResponseDto findMyProductByUser(
            User user,
            Pageable pageable
    ) {
        if (user.getProvider() == ProviderEnum.FALSE) {
            throw new HttpException(
                    false,
                    "PROVIDER 가 아닙니다.",
                    HttpStatus.NOT_FOUND
            );
        }

        Page<Product> productPage = productRepository.findByUserId(user.getId(), pageable);
        List<Product> products = productPage.getContent();
        return new FindProductResponseDto(
                productDtos(products),
                products.size(),
                productPage.getTotalPages()
        );
    }

    public List<Comment> toDtoList(List<Comment> comments) {
        return comments.stream()
                .map(comment -> {
                    Comment dto = new Comment();
                    dto.setId(comment.getId());
                    dto.setRating(comment.getRating());
                    dto.setDescription(comment.getDescription());
                    dto.setImg(comment.getImg());
                    dto.setCreatedAt(comment.getCreatedAt());
                    dto.setUpdatedAt(comment.getUpdatedAt());
                    dto.setDeletedAt(comment.getDeletedAt());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<ProductDto> productDtos(
            List<Product> products
    ) {
        return products.stream()
                .map(product -> new ProductDto(
                        product.getId(),
                        product.getName(),
                        product.getPosition(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getImg()
                ))
                .collect(Collectors.toList());
    }
}
