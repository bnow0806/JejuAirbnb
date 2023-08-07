package com.example.jejuairbnb.controller.UserControllerDto.MyInfoUserDto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MyInfoUserResponseDto {
    private String email;
    private String username;
}
