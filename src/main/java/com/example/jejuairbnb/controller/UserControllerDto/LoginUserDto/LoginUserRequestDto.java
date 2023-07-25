package com.example.jejuairbnb.controller.UserControllerDto.LoginUserDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginUserRequestDto {
    private String kakaoToken;
}
