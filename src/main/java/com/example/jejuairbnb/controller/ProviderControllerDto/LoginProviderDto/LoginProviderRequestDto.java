package com.example.jejuairbnb.controller.ProviderControllerDto.LoginProviderDto;

import lombok.Builder;
import lombok.Data;

@Data
public class LoginProviderRequestDto {
    private String username;
    private String email;

    @Builder
    public LoginProviderRequestDto(
            String username,
            String email
    ) {
        this.username = username;
        this.email = email;
    }
}
