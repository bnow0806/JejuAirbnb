package com.example.jejuairbnb.controller.UserControllerDto.FindUserDto;

import lombok.Builder;
import lombok.Data;

@Data
public class FindUserResponseDto {

    private Long userId;
    private String email;
    private String username;

    @Builder
    public FindUserResponseDto(
            Long userId,
            String email,
            String username
    ) {
        this.userId = userId;
        this.email = email;
        this.username = username;
    }
}
