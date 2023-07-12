package com.example.jejuairbnb.controller.ProviderControllerDto.UpdateProviderDto;

import lombok.Builder;
import lombok.Data;

@Data
public class UpdateProviderRequestDto {

    private String providername;
    private String email;
    private String password;
    private String rePassword;

    @Builder
    public UpdateProviderRequestDto(
            String providername,
            String password,
            String rePassword
    ) {
        this.providername = providername;
        this.password = password;
        this.rePassword = rePassword;
    }
}
