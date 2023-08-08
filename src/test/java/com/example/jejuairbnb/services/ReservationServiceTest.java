package com.example.jejuairbnb.services;

import com.example.jejuairbnb.domain.Reservation;
import com.example.jejuairbnb.repository.ICommentRepository;
import com.example.jejuairbnb.repository.IProductRepository;
import com.example.jejuairbnb.repository.IReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.event.annotation.BeforeTestClass;

@SpringBootTest
public class ReservationServiceTest {
    @MockBean
    private IReservationRepository reservationRepository;

    @MockBean
    private IProductRepository productRepository;

    private ReservationService reservationService;

    @BeforeEach
    public void setup() {
        reservationService = new ReservationService(
            reservationRepository,
                productRepository
        );
    }

    @Test
    public void createSuccessReservation() {

    }
}
