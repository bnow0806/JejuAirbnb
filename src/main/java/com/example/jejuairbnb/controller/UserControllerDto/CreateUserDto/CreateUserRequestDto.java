package com.example.jejuairbnb.controller.UserControllerDto.CreateUserDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequestDto {
    private String kakaoToken;
}
