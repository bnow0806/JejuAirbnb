package com.example.jejuairbnb.controller.UserControllerDto.CreateUserDto;

import lombok.Builder;
import lombok.Data;

@Data
public class CreateUserRequestDto {
    private String kakaoToken;

    @Builder
    public CreateUserRequestDto(
            String kakaoToken
    ) {
        this.kakaoToken = kakaoToken;
    }
}
