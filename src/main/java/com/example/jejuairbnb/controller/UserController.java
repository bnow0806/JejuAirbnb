package com.example.jejuairbnb.controller;

import com.example.jejuairbnb.controller.UserControllerDto.CreateUserDto.CreateUserRequestDto;
import com.example.jejuairbnb.controller.UserControllerDto.CreateUserDto.CreateUserResponseDto;
import com.example.jejuairbnb.controller.UserControllerDto.LoginUserDto.LoginUserRequestDto;
import com.example.jejuairbnb.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

@RestController
@Tag(name = "user", description = "유저 API")
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/kakao_login")
    public CreateUserResponseDto registerUser(
            @RequestBody CreateUserRequestDto requestDto,
            HttpServletResponse httpServletResponse
    ) {
        return userService.registerUser(
                requestDto,
                httpServletResponse
        );
    }
}
