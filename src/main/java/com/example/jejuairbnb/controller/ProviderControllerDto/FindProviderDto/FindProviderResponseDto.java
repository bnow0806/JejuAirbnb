package com.example.jejuairbnb.controller.ProviderControllerDto.FindProviderDto;

import lombok.Builder;
import lombok.Data;

@Data
public class FindProviderResponseDto {

    private Long providerId;
    private String email;

    private String providername;

    @Builder
    public FindProviderResponseDto(
            Long providerId,
            String email,
            String providername
    ) {
        this.providerId = providerId;
        this.email = email;
        this.providername = providername;
    }
}
