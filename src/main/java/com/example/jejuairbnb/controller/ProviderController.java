package com.example.jejuairbnb.controller;

import com.example.jejuairbnb.controller.ProductControllerDto.FindProductResponseDto;
import com.example.jejuairbnb.domain.User;
import com.example.jejuairbnb.services.ProductService;
import com.example.jejuairbnb.shared.services.SecurityService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.data.domain.Pageable;

@RestController
@Tag(name = "user", description = "유저 API")
@AllArgsConstructor
@RequestMapping("/api/providers")
public class ProviderController {
    private final ProductService productService;
    private final SecurityService securityService;

    @GetMapping("/my_provider_info")
    public FindProductResponseDto getMyInfo(
            @CookieValue("access-token") String accessToken,
            Pageable pageable
    ) {
        User findUser = securityService.getSubject(accessToken);
        return productService.findMyProductByUser(
                findUser,
				pageable
        );
    }
}
