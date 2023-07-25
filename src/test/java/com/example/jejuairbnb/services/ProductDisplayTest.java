package com.example.jejuairbnb.services;

import com.example.jejuairbnb.adminServices.AdminProductService;
import com.example.jejuairbnb.repository.IProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class ProductDisplayTest {

    @MockBean
    private IProductRepository productRepository;

    @Autowired
    private AdminProductService adminProductService;

    @Test
    public void testReadProductByPage(){}

    @Test
    public void testReadProductTotalConunt(){}

    @Test
    public void testReadProductTotalPage(){}

    @Test
    public void testReadProductByPageNumber(){}
}
