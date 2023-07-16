package com.example.jejuairbnb.controller.ProviderControllerDto.LoginProviderDto;

import lombok.Builder;
import lombok.Data;

@Data
public class LoginProviderResponseDto
{
    private String email;
    private String token;

}
