package com.example.jejuairbnb.services;

import com.example.jejuairbnb.adminController.AdminProductDto.CreateProductDto.CreateProductRequestDto;
import com.example.jejuairbnb.adminServices.AdminProductService;
import com.example.jejuairbnb.domain.User;
import com.example.jejuairbnb.repository.IProductRepository;
import com.example.jejuairbnb.shared.Enum.ProviderEnum;
import com.example.jejuairbnb.shared.response.CoreSuccessResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;


@SpringBootTest
public class AdminProductServiceTest {

    @MockBean
    private IProductRepository productRepository;

    @Autowired
    private AdminProductService adminProductService;

    @Test
    public void testCreateProduct() {
        // 주어진 User와 CreateProductRequestDto 객체 생성
        User user = new User();
        user.setProvider(ProviderEnum.TRUE);

        CreateProductRequestDto requestDto = new CreateProductRequestDto();
        requestDto.setName("Test Product");
        requestDto.setPrice(100);
        requestDto.setImg("Test Image URL");
        requestDto.setUserId(1L);

        // 실행
        CoreSuccessResponse response = adminProductService.createProduct(user, requestDto);

        // 검증
        Assertions.assertEquals(response.getMessage(), "상품이 등록되었습니다.");
        Assertions.assertEquals(response.getStatusCode(), 201);
        Assertions.assertTrue(response.isOk());
    }

    @Test()
    public void testCreateProductFailTest() {
        User user = new User();
        user.setId(1L);
        user.setProvider(ProviderEnum.FALSE);
        user.setUsername("Test User");
        user.setEmail("Test Email");

        Assertions.assertEquals(user.getProvider(), ProviderEnum.FALSE);
        Assertions.assertEquals(user.getId(), 1L);
        Assertions.assertEquals(user.getUsername(), "Test User");
    }
}
