package com.green.glampick.repository;

import com.green.glampick.entity.ReservationBeforeEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReservationBeforeRepositoryTest {

    @Autowired
    private ReservationBeforeRepository reservationBeforeRepository;

    @Test
    public void testFindAllByCheckOutDateBefore() {
        LocalDate currentDateTime = LocalDate.now();
        List<ReservationBeforeEntity> expiredReservations = reservationBeforeRepository.findAllByCheckOutDateBefore(currentDateTime);

        assertNotNull(expiredReservations);
        System.out.println("Found expired reservations: " + expiredReservations.size());
        for (ReservationBeforeEntity reservation : expiredReservations) {
            System.out.println("Reservation ID: " + reservation.getReservationId());
        }
    }

}