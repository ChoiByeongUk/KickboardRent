package com.knu.ssingssing2.service;

import static java.util.stream.Collectors.toList;

import com.knu.ssingssing2.exception.BadRequestException;
import com.knu.ssingssing2.exception.ReservationNotFoundException;
import com.knu.ssingssing2.exception.ResourceNotFoundException;
import com.knu.ssingssing2.exception.ScooterNotFoundException;
import com.knu.ssingssing2.exception.UnavailableException;
import com.knu.ssingssing2.model.Location;
import com.knu.ssingssing2.model.User;
import com.knu.ssingssing2.model.reservation.Reservation;
import com.knu.ssingssing2.model.reservation.ReservationLocation;
import com.knu.ssingssing2.model.reservation.ReservationState;
import com.knu.ssingssing2.model.reservation.ReservationTime;
import com.knu.ssingssing2.model.scooter.Scooter;
import com.knu.ssingssing2.payload.response.ApiResponse;
import com.knu.ssingssing2.repository.ReservationRepository;
import com.knu.ssingssing2.repository.ScooterRepository;
import com.knu.ssingssing2.repository.UserRepository;
import com.knu.ssingssing2.security.UserPrincipal;
import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservationService {

  @Autowired
  private UserRepository userRepository;

  private final ScooterRepository scooterRepository;

  private final ReservationRepository reservationRepository;

  private final Logger logger = LoggerFactory.getLogger(ReservationService.class);

  @Autowired
  public ReservationService(ScooterRepository scooterRepository,
      ReservationRepository reservationRepository) {
    this.scooterRepository = scooterRepository;
    this.reservationRepository = reservationRepository;
  }

  @Transactional
  public ApiResponse reservation(UserPrincipal currentUser, Long scooterId, Location returnLocation,
      ReservationTime reservationTime) {
    User user = userRepository.findOneById(currentUser.getId());

    Scooter scooter = scooterRepository.findOneById(scooterId);
    if (scooter == null) {
      throw new ScooterNotFoundException("can not find scooter");
    }

    if (!scooter.isAvailable() || scooter.isReserved(reservationTime)) {
      throw new UnavailableException("scooter is already reserved");
    }

    Location rentalLocation = scooter.getLocation();
    ReservationLocation location = new ReservationLocation(rentalLocation, returnLocation);
    Reservation reservation = Reservation.builder()
        .scooter(scooter)
        .location(location)
        .reservationTime(reservationTime)
        .reservationState(ReservationState.RESERVED)
        .build();

    scooter.addReservation(reservation);
    user.addReservation(reservation);

    reservationRepository.save(reservation);

    return new ApiResponse(true, "scooter reservation completed");
  }

  public List<Scooter> findAllAvailableReservationScootersWithModelAndLocation(
      String modelName,
      String rentalLocation, ReservationTime time) {
    List<Scooter> scooters = scooterRepository.findAllByModelName(modelName);
    return streamData(rentalLocation, time, scooters);
  }

  public List<Scooter> findAllAvailableReservationScootersWithLocation(
      String rentalLocation,
      ReservationTime time) {
    List<Scooter> scooters = scooterRepository.findAll();
    return streamData(rentalLocation, time, scooters);
  }

  @Transactional
  public void cancelReservation(UserPrincipal currentUser, Long reservationId) {
    User user = userRepository.findOneById(currentUser.getId());
    Reservation reservation = reservationRepository.findOneById(reservationId);

    if (reservation == null) {
      logger.info("잘못된 reservationId 입니다.");
      throw new ReservationNotFoundException("can not find reservation");
    }

    if (!reservation.getReservationState().equals(ReservationState.RESERVED)) {
      logger.info("이미 사용되거나 취소된 예약 내역을 취소할 수 없습니다.");
      throw new BadRequestException("can not cancel reservation");
    }

    user.removeReservation(reservation);
    reservation.changeStateToCanceled();
  }

  private List<Scooter> streamData(String rentalLocation, ReservationTime time,
      List<Scooter> scooters) {
    return scooters.stream()
        .filter(scooter -> scooter.isAvailableReservation(time))
        .filter(scooter -> {
          if (rentalLocation == null) {
            return true;
          }
          return scooter.getLocation().equals(rentalLocation);
        }).collect(toList());
  }

  public List<Reservation> getReservationsByUser(String username, UserPrincipal currentUser) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    if (!currentUser.getUsername().equals(username)) {
      throw new UnavailableException("user not matched");
    }

    return user.getReservations();
  }

  public List<Reservation> getAvailableReservationsByUser(String username,
      UserPrincipal currentUser) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    if (!currentUser.getUsername().equals(username)) {
      throw new UnavailableException("user not matched");
    }

    return user.getReservations().stream()
        .filter(r -> r.getReservationTime().getStartTime().compareTo(LocalDateTime.now()) >= 0)
        .collect(toList());
  }
}
