package com.example.jejuairbnb.controller.UserControllerDto.CreateUserDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CreateUserResponseDto {
    private String username;
    private String email;
}
