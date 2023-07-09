package com.example.jejuairbnb.controller.UserControllerDto.FindUserDto;

import lombok.Builder;
import lombok.Data;

@Data
public class FindUserRequestDto {
    private int userId;

    @Builder
    public FindUserRequestDto(
            int userId
    ) {
        this.userId = userId;
    }
}
