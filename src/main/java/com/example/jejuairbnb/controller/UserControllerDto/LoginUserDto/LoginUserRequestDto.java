package com.example.jejuairbnb.controller.UserControllerDto.LoginUserDto;

import lombok.Builder;
import lombok.Data;

@Data
public class LoginUserRequestDto {
    private String email;
    private String password;

    @Builder
    public LoginUserRequestDto(
            String email,
            String password
    ) {
        this.email = email;
        this.password = password;
    }
}
