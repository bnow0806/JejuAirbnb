package com.example.jejuairbnb.services;

import com.example.jejuairbnb.controller.ReservationControllerDto.CreateReservationDto.CreateReservationRequestDto;
import com.example.jejuairbnb.controller.ReservationControllerDto.CreateReservationDto.CreateReservationResponseDto;
import com.example.jejuairbnb.domain.Product;
import com.example.jejuairbnb.domain.Reservation;
import com.example.jejuairbnb.domain.User;
import com.example.jejuairbnb.repository.IProductRepository;
import com.example.jejuairbnb.repository.IReservationRepository;
import com.example.jejuairbnb.shared.exception.HttpException;
import com.example.jejuairbnb.shared.response.CoreSuccessResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


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
        Long productId = createReservationRequestDto.getProductId();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate checkInDate = LocalDate.parse(checkIn, formatter);
        LocalDate checkOutDate = LocalDate.parse(checkOut, formatter);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new HttpException(
                        false,
                        "해당 상품이 존재하지 않습니다.",
                        HttpStatus.NOT_FOUND
                ));

        List<Reservation> reservationList = product.getReservations();
        for (Reservation reservation: reservationList) {
            LocalDate existingCheckIn = LocalDate.parse(reservation.getCheckIn(), formatter);
            LocalDate existingCheckOut = LocalDate.parse(reservation.getCheckOut(), formatter);

            if ((checkOutDate.isEqual(existingCheckIn) || checkInDate.isAfter(existingCheckIn)) && checkInDate.isBefore(existingCheckOut)
                    || (checkOutDate.isAfter(existingCheckIn) && checkOutDate.isBefore(existingCheckOut) || checkOutDate.isEqual(existingCheckOut))
                    || (checkInDate.isBefore(existingCheckIn) && checkOutDate.isAfter(existingCheckOut))) {
                throw new HttpException(
                        false,
                        "이미 예약된 날짜입니다.",
                        HttpStatus.BAD_REQUEST
                );
            }
        }

        Reservation newReservation = Reservation
                .builder()
                .checkIn(checkIn)
                .checkOut(checkOut)
                .product(product)
                .userId(user.getId())
                .build();

        Reservation savedReservation = reservationRepository.save(newReservation);

        Product findProduct = productRepository.findById(productId)
                .orElseThrow(() -> new HttpException(
                        false,
                        "해당 상품이 존재하지 않습니다.",
                        HttpStatus.NOT_FOUND
                ));
        int price = findProduct.getPrice();
        int totalPrice;

        long daysBetween = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        totalPrice = (int)daysBetween * price;

        return new CreateReservationResponseDto(
                savedReservation.getProduct().getId(),
                savedReservation.getUserId(),
                savedReservation.getCheckIn(),
                savedReservation.getCheckOut(),
                totalPrice
        );
    }

    @Transactional
    public CoreSuccessResponse deleteReservation(
            User user,
            Long id
    ) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(id);

        if (optionalReservation.isPresent()) {
            Reservation reservation = optionalReservation.get();

            // 사용자 권한 체크 (예시: 예약한 사용자와 동일한지)
            if (!reservation.getUserId().equals(user.getId())) {
                throw new HttpException(
                        false,
                        "해당 예약을 삭제할 권한이 없습니다.",
                        HttpStatus.FORBIDDEN
                );
            }

            // 실제로 데이터를 삭제하는 대신 deleted_at에 현재 시간을 설정
            reservation.setDeletedAt(LocalDateTime.now());
            reservationRepository.save(reservation);

            return new CoreSuccessResponse(
                    true,
                    "예약이 취소 되었습니다.",
                    HttpStatus.OK.value()
            );
        } else {
            throw new HttpException(
                    false,
                    "예약 정보를 찾을 수 없습니다.",
                    HttpStatus.NOT_FOUND
            );
        }
    }
}
