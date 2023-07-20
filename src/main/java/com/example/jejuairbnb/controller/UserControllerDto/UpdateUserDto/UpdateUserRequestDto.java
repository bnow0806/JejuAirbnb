package com.example.jejuairbnb.controller.UserControllerDto.UpdateUserDto;

import lombok.Builder;
import lombok.Data;

@Data
public class UpdateUserRequestDto {

    private String username;
    private String email;

    @Builder
    public UpdateUserRequestDto(
            String email,
            String username
    ) {
        this.email = email;
        this.username = username;
    }
}
