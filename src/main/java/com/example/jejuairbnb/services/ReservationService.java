package com.example.jejuairbnb.services;

import com.example.jejuairbnb.controller.ReservationControllerDto.CreateReservationDto.CreateReservationRequestDto;
import com.example.jejuairbnb.controller.ReservationControllerDto.CreateReservationDto.CreateReservationResponseDto;
import com.example.jejuairbnb.domain.Product;
import com.example.jejuairbnb.domain.Reservation;
import com.example.jejuairbnb.domain.User;
import com.example.jejuairbnb.repository.IProductRepository;
import com.example.jejuairbnb.repository.IReservationRepository;
import com.example.jejuairbnb.shared.exception.HttpException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;


@Service
@AllArgsConstructor
public class ReservationService {
    private final IReservationRepository reservationRepository;
    private final IProductRepository productRepository;

    @Transactional
    public CreateReservationResponseDto createReservation(
            User user,
            CreateReservationRequestDto createReservationRequestDto
    ) {

        String checkIn = createReservationRequestDto.getCheckIn();
        String checkOut = createReservationRequestDto.getCheckOut();

        Reservation newReservation = Reservation
                .builder()
                .checkIn(createReservationRequestDto.getCheckIn())
                .checkOut(createReservationRequestDto.getCheckOut())
                .productId(createReservationRequestDto.getProductId())
                .userId(user.getId())
                .build();

        Reservation savedReservation = reservationRepository.save(newReservation);

        Product findProduct = productRepository.findById(createReservationRequestDto.getProductId())
                .orElseThrow(() -> new HttpException(
                        false,
                        "해당 상품이 존재하지 않습니다.",
                        HttpStatus.NOT_FOUND
                ));
        int price = findProduct.getPrice();
        int totalPrice;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate checkInDate = LocalDate.parse(checkIn,formatter);
        LocalDate checkOutDate = LocalDate.parse(checkOut,formatter);

        long daysBetween = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        totalPrice = (int)daysBetween * price;

        return new CreateReservationResponseDto(
                savedReservation.getProductId(),
                savedReservation.getUserId(),
                savedReservation.getCheckIn(),
                savedReservation.getCheckOut(),
                totalPrice
        );
    }
}
