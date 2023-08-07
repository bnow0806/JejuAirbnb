package com.example.jejuairbnb.controller.ProviderControllerDto.CreateProviderDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateProviderRequestDto {
    private String kakaoToken;
}
