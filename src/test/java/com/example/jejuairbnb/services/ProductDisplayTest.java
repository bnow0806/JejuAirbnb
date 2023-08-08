package com.example.jejuairbnb.services;

import com.example.jejuairbnb.adminServices.AdminProductService;
import com.example.jejuairbnb.domain.Product;
import com.example.jejuairbnb.repository.IProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class ProductDisplayTest {

    @MockBean
    //@Autowired
    private IProductRepository productRepository;

    @Autowired
    private AdminProductService adminProductService;

    @Test
    public void testReadProductByPageNumber(){
        //given
        List<Product> products = BulkInsert();
        List<Product> readProducts = products.subList(10,20);

        Mockito.when(productRepository.findByPageNumber(11L,20L)).thenReturn(readProducts);

        //when
        //특정 페이지에 있는 상품만 불러오기
        List<Product> findProducts = productRepository.findByPageNumber(11L,20L);
        System.out.println("[Debug] findProducts"+findProducts);

        //then
        Assertions.assertEquals(10, findProducts.size());
        Assertions.assertEquals(11L, findProducts.get(0).getId());
        Assertions.assertEquals(20L, findProducts.get(9).getId());
    }

    @Test
    public void testReadProductTotalConunt(){
        // given
        List<Product> products = BulkInsert();

        Mockito.when(productRepository.findAll()).thenReturn(products);

        // when
        List<Product> findProducts = productRepository.findAll();

        //then
        Assertions.assertEquals(20, findProducts.size());
    }

    @Test
    public void testReadProductTotalPage(){
        // given
        List<Product> products = BulkInsert();

        Mockito.when(productRepository.findAll()).thenReturn(products);

        // when
        List<Product> findProducts = productRepository.findAll();
        int viewCount = 10;
        int page = findProducts.size() / viewCount;

        //then
        Assertions.assertEquals(2, page);
    }

    private List<Product> BulkInsert(){
        // given
        long count = 20L;   //insert size

        List<Product> products = new ArrayList<>();
        while(count --> 0) {

            long productId = 20L-count;
            Product product = Product.builder()
                    .id(productId)  //1, 2, 3, 4...
                    .name("Test product"+Long.toString(productId))
                    .price(100L)
                    .img("Test image"+Long.toString(productId))
                    .userId(1L)
                    .build();

            products.add(product);
        }

        // when
        productRepository.saveAll(products);
        //System.out.println("[Debug] products"+products);

        // then
        Assertions.assertEquals(20, products.size());

        return products;
    }
}
