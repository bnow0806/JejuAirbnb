package com.example.jejuairbnb.controller.UserControllerDto;

import lombok.Builder;
import lombok.Data;

@Data
public class UserControllerRequestDto {
    private String username;
    private String password;
    private String email;
    private String rePassword;

    @Builder
    public UserControllerRequestDto(
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
