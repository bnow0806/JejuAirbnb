package com.example.jejuairbnb.controller.ReservationControllerDto.CreateReservationDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateReservationResponseDto {
    private Long productId;
    private Long userId;
    private String checkIn;
    private String checkOut;
    private int price;
}
