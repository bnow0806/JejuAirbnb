package com.example.jejuairbnb.controller.ProviderControllerDto.CreateProviderDto;

import lombok.Builder;
import lombok.Data;

@Data
public class CreateProviderRequestDto {
    private String providername;
    private String password;
    private String email;
    private String rePassword;

    @Builder
    public CreateProviderRequestDto(
            String providername,
            String password,
            String email,
            String rePassword
    ) {
        this.providername = providername;
        this.password = password;
        this.email = email;
        this.rePassword = rePassword;
    }
}
