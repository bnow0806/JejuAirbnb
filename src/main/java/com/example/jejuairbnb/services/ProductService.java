package com.example.jejuairbnb.services;

import com.example.jejuairbnb.controller.ProductControllerDto.FindProductOneResponseDto;
import com.example.jejuairbnb.controller.ProductControllerDto.FindProductResponseDto;
import com.example.jejuairbnb.domain.Comment;
import com.example.jejuairbnb.domain.Product;
import com.example.jejuairbnb.domain.User;
import com.example.jejuairbnb.repository.ICommentRepository;
import com.example.jejuairbnb.repository.IProductRepository;
import com.example.jejuairbnb.shared.Enum.ProviderEnum;
import com.example.jejuairbnb.shared.exception.HttpException;
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

    public FindProductResponseDto findProduct(
            Pageable pageable
    ) {
        try {
            Page<Product> productPage = productRepository.findAll(pageable);
            // productPage 에 나온 product 들의 comment 수를 구한다.
            for (Product product : productPage.getContent()) {
                product.setCommentCount(commentRepository.countByProductId(product.getId()));
                product.setCommentAvg(commentRepository.avgByProductId(product.getId()));
            }

            return FindProductResponseDto
                    .builder()
                    .products(productPage.getContent())
                    .size(productPage.getSize())
                    .totalPages(productPage.getTotalPages())
                    .build();
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
                products,
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
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
