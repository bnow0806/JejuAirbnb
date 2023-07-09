package com.example.jejuairbnb.controller.UserControllerDto.CreateUserDto;

import lombok.Builder;
import lombok.Data;

@Data
public class CreateUserRequestDto {
    private String username;
    private String password;
    private String email;
    private String rePassword;

    @Builder
    public CreateUserRequestDto(
            String username,
            String password,
            String email,
            String rePassword
    ) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.rePassword = rePassword;
    }
}
