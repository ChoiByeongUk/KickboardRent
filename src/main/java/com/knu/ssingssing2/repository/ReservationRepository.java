package com.knu.ssingssing2.repository;

import com.knu.ssingssing2.model.reservation.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

  Reservation findOneById(Long id);

}
