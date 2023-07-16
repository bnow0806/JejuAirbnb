package com.example.jejuairbnb.controller.ProviderControllerDto.LoginProviderDto;

import lombok.Builder;
import lombok.Data;

@Data
public class LoginProviderRequestDto {
    private String password;
    private String email;

    @Builder
    public LoginProviderRequestDto(
            String password,
            String email
    ) {
        this.password = password;
        this.email = email;
    }
}
