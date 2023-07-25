package com.example.jejuairbnb.controller;

import com.example.jejuairbnb.controller.UserControllerDto.CreateUserDto.CreateUserRequestDto;
import com.example.jejuairbnb.controller.UserControllerDto.CreateUserDto.CreateUserResponseDto;
import com.example.jejuairbnb.controller.UserControllerDto.LoginUserDto.LoginUserRequestDto;
import com.example.jejuairbnb.controller.UserControllerDto.MyInfoUserDto.MyInfoUserResponseDto;
import com.example.jejuairbnb.domain.User;
import com.example.jejuairbnb.services.UserService;
import com.example.jejuairbnb.shared.services.SecurityService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RestController
@Tag(name = "user", description = "유저 API")
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final SecurityService securityService;

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

    @GetMapping("/my_info")
    public MyInfoUserResponseDto getMyInfo(
            @RequestHeader("Authorization") String token,
            @CookieValue("access-token") String accessToken
    ) {
        User findUser = securityService.getSubject(accessToken);
        return userService.getMyInfo(
                findUser
        );
    }

}
