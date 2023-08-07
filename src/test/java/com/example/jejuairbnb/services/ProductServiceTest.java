package com.example.jejuairbnb.services;

import com.example.jejuairbnb.controller.ProductControllerDto.FindProductOneResponseDto;
import com.example.jejuairbnb.domain.Comment;
import com.example.jejuairbnb.domain.Product;
import com.example.jejuairbnb.repository.ICommentRepository;
import com.example.jejuairbnb.repository.IProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class ProductServiceTest {

    @MockBean
    private IProductRepository productRepository;

    @MockBean
    private ICommentRepository commentRepository;

    private ProductService productService;

    @BeforeEach
    public void setup() {
        productService = new ProductService(
                productRepository,
                commentRepository
        );
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

        Product mockProduct3 = new Product();
        mockProduct2.setId(3L);
        mockProduct2.setName("Test product 3");
        mockProduct2.setImg("Test image 3");
        mockProduct2.setPrice(300);

        Product mockProduct4 = new Product();
        mockProduct2.setId(4L);
        mockProduct2.setName("Test product 4");
        mockProduct2.setImg("Test image 4");
        mockProduct2.setPrice(400);

        int page = 1;
        int size = 2;
        Pageable pageable = PageRequest.of(page - 1, size);

        List<Product> mockProducts = Arrays.asList(
                mockProduct1,
                mockProduct2,
                mockProduct3,
                mockProduct4
        );
        Page<Product> productPage = new PageImpl<>(mockProducts);
        Mockito.when(productRepository.findAll(pageable)).thenReturn(productPage);

        // When
        Page<Product> resultPage = productRepository.findAll(pageable);
        List<Product> products = resultPage.getContent();

        System.out.println("products:"+products);
        // Then
        Assertions.assertEquals(4, products.size());
        Assertions.assertEquals(mockProduct1, products.get(0));
        Assertions.assertEquals(mockProduct2, products.get(1));
    }

    @Test
    public void FindOneProductWithComments() {
        // Given
        long productId = 1L;
        Product mockProduct = new Product();
        mockProduct.setId(productId);
        mockProduct.setName("Test Product 1");
        mockProduct.setImg("Test Product image 1");
        mockProduct.setPrice(100);

        Comment mockComment1 = new Comment();
        mockComment1.setId(1L);
        mockComment1.setRating(1.1f);
        mockComment1.setDescription("Test comment 1");
        mockComment1.setProduct(mockProduct);

        Comment mockComment2 = new Comment();
        mockComment2.setId(2L);
        mockComment2.setRating(1.2f);
        mockComment2.setDescription("Test comment 2");
        mockComment2.setProduct(mockProduct);

        Comment mockComment3 = new Comment();
        mockComment3.setId(3L);
        mockComment3.setRating(1.3f);
        mockComment3.setDescription("Test comment 3");
        mockComment3.setProduct(mockProduct);

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct));
        Mockito.when(commentRepository.countByProductId(productId)).thenReturn(3L);  // example value
        Mockito.when(commentRepository.avgByProductId(productId)).thenReturn(1.2);  // example value
        Mockito.when(commentRepository.findByProductId(productId)).thenReturn(Arrays.asList(mockComment1, mockComment2, mockComment3));

        // When
        FindProductOneResponseDto result = productService.findProductOneById(productId);
        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals("Test Product 1", result.getName());
        Assertions.assertEquals("Test Product image 1", result.getImg());
        Assertions.assertEquals(100, result.getPrice());

        Assertions.assertEquals(3, result.getComments().size());
        Assertions.assertEquals("Test comment 2", result.getComments().get(1).getDescription());
        Assertions.assertEquals("Test comment 3", result.getComments().get(2).getDescription());
    }
}
