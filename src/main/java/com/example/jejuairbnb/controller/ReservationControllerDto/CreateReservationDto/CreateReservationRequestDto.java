package com.example.jejuairbnb.controller.ReservationControllerDto.CreateReservationDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CreateReservationRequestDto {
    private String checkIn;
    private String checkOut;
    private Long productId;
}
