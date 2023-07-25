package com.example.jejuairbnb.services;

import com.example.jejuairbnb.controller.ProductControllerDto.FindProductOneResponseDto;
import com.example.jejuairbnb.domain.Product;
import com.example.jejuairbnb.repository.IProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class ProductServiceTest {

    @MockBean
    private IProductRepository productRepository;

    private ProductService productService;

    @BeforeEach
    public void setup() {
        productService = new ProductService(productRepository);
    }

    @Test
    public void testFindOneProductById() {
        // given

        Product mockProduct = new Product();
        mockProduct.setId(1L);
        mockProduct.setName("Test product");
        mockProduct.setImg("Test image");
        mockProduct.setPrice(100);
        Mockito.when(productRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(mockProduct));

        // When
        FindProductOneResponseDto response = productService.findProductOneById(1L);

        // Then
        Assertions.assertEquals(mockProduct.getName(), response.getName());
        Assertions.assertEquals(mockProduct.getImg(), response.getImg());
        Assertions.assertEquals(mockProduct.getPrice(), response.getPrice());
    }

    @Test
    public void FindAllProduct() {

// Given
        Product mockProduct1 = new Product();
        mockProduct1.setId(1L);
        mockProduct1.setName("Test product 1");
        mockProduct1.setImg("Test image 1");
        mockProduct1.setPrice(100);

        Product mockProduct2 = new Product();
        mockProduct2.setId(2L);
        mockProduct2.setName("Test product 2");
        mockProduct2.setImg("Test image 2");
        mockProduct2.setPrice(200);

        List<Product> mockProducts = Arrays.asList(mockProduct1, mockProduct2);
        Mockito.when(productRepository.findAll()).thenReturn(mockProducts);

        // When
        List<Product> products = productRepository.findAll();

        // Then
        Assertions.assertEquals(2, products.size());
        Assertions.assertEquals(mockProduct1, products.get(0));
        Assertions.assertEquals(mockProduct2, products.get(1));
    }
}
