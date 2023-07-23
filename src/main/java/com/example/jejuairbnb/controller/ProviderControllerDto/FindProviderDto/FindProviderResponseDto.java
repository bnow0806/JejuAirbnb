package com.example.jejuairbnb.controller.ProviderControllerDto.FindProviderDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FindProviderResponseDto {

    private Long providerId;
    private String email;
    private String username;
}
