package com.example.jejuairbnb.services;

import com.example.jejuairbnb.adminServices.AdminProductService;
import com.example.jejuairbnb.controller.ProductControllerDto.FindProductDto.FindProductOneResponseDto;
import com.example.jejuairbnb.domain.Product;
import com.example.jejuairbnb.repository.IProductRepository;
import com.sun.source.tree.ModuleTree;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class ProductSearchTest
{
    @MockBean
    private IProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @BeforeEach
    public void setup() {
        productService = new ProductService(productRepository);
    }

    @Test
    public void testFindProductByName(){

        //given
        Product mockProduct = new Product();
        mockProduct.setId(1L);
        mockProduct.setName("Test product");
        mockProduct.setImg("Test image");
        mockProduct.setPrice(100L);

        //findByName
        Mockito.when(productRepository.findByName("Test product")).thenReturn(Optional.of(mockProduct));

        // When
        Product findProduct = productRepository.findByName("Test product").orElse(null);

        // Then
        Assertions.assertEquals(mockProduct.getName(), findProduct.getName());
        Assertions.assertEquals(mockProduct.getImg(), findProduct.getImg());
        Assertions.assertEquals(mockProduct.getPrice(), findProduct.getPrice());
    }

    @Test
    public void testFindProductByKeywordUserId(){
        // Given
        //TODO : Test 마다 매번 실해시키고, mockProduct1 변수를 불러오는 방법?
        Product mockProduct1 = new Product();
        mockProduct1.setId(1L);
        mockProduct1.setName("Test product 1");
        mockProduct1.setImg("Test image 1");
        mockProduct1.setPrice(100L);
        mockProduct1.setUserId(1L);

        Product mockProduct2 = new Product();
        mockProduct2.setId(2L);
        mockProduct2.setName("Test product 2");
        mockProduct2.setImg("Test image 2");
        mockProduct2.setPrice(200L);
        mockProduct2.setUserId(2L);

        Product mockProduct3 = new Product();
        mockProduct3.setId(3L);
        mockProduct3.setName("Test product 3");
        mockProduct3.setImg("Test image 3");
        mockProduct3.setPrice(300L);
        mockProduct3.setUserId(1L);

        List<Product> mockProducts = Arrays.asList(mockProduct1, mockProduct3);

        Mockito.when(productRepository.findByKeywordUserId(1L)).thenReturn(mockProducts);

        // When
        List<Product> products = productRepository.findByKeywordUserId(1L);
        //System.out.println("(Debug) products "+products);

        // Then
        Assertions.assertEquals(2, products.size());
        Assertions.assertEquals(mockProduct1, products.get(0));
        Assertions.assertEquals(mockProduct3, products.get(1));
    }

    @Test
    public void testFindProductByKeywordPrice(){
        // Given
        Product mockProduct1 = new Product();
        mockProduct1.setId(1L);
        mockProduct1.setName("Test product 1");
        mockProduct1.setImg("Test image 1");
        mockProduct1.setPrice(100L);
        mockProduct1.setUserId(1L);

        Product mockProduct2 = new Product();
        mockProduct2.setId(2L);
        mockProduct2.setName("Test product 2");
        mockProduct2.setImg("Test image 2");
        mockProduct2.setPrice(200L);
        mockProduct2.setUserId(2L);

        List<Product> mockProducts = Arrays.asList(mockProduct1);

        Mockito.when(productRepository.findByKeywordPrice(150L)).thenReturn(mockProducts);

        // When
        List<Product> products = productRepository.findByKeywordPrice(150L);

        // Then
        Assertions.assertEquals(1, products.size());
        Assertions.assertEquals(mockProduct1, products.get(0));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> products.get(1));
    }

    @Test
    public void testFindProductByFacility(){}

    @Test
    public void testFindAvailableProductByDay(){}

}
