package com.example.jejuairbnb.controller.ProviderControllerDto.CreateProviderDto;

import com.example.jejuairbnb.shared.Enum.ProviderEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateProviderRequestDto {
    private String username;
    private String email;
    private ProviderEnum provider;
    private String kakaoAuthId;
}
