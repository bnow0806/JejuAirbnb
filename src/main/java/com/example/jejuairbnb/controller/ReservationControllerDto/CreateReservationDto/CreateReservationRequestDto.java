package com.example.jejuairbnb.controller.ReservationControllerDto.CreateReservationDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateReservationRequestDto {
    private String checkIn;
    private String checkOut;
    private Long productId;
}
