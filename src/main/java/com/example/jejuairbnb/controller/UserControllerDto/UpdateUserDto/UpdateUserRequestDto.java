package com.example.jejuairbnb.controller.UserControllerDto.UpdateUserDto;

import lombok.Builder;
import lombok.Data;

@Data
public class UpdateUserRequestDto {

    private String username;
    private String email;
    private String password;
    private String rePassword;

    @Builder
    public UpdateUserRequestDto(
            String email,
            String username,
            String password,
            String rePassword
    ) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.rePassword = rePassword;
    }
}
