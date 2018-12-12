package com.knu.ssingssing2.service;

import com.knu.ssingssing2.exception.BadRequestException;
import com.knu.ssingssing2.model.Location;
import com.knu.ssingssing2.model.reservation.Reservation;
import com.knu.ssingssing2.model.scooter.Scooter;
import com.knu.ssingssing2.model.scooter.ScooterState;
import com.knu.ssingssing2.payload.request.ScooterLocationRequest;
import com.knu.ssingssing2.payload.request.ScooterReturnRequest;
import com.knu.ssingssing2.payload.response.PagedResponse;
import com.knu.ssingssing2.repository.ReservationRepository;
import com.knu.ssingssing2.repository.ScooterRepository;
import com.knu.ssingssing2.repository.UserRepository;
import com.knu.ssingssing2.security.UserPrincipal;
import com.knu.ssingssing2.util.AppConstants;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ScooterService {

  @Autowired
  private ReservationRepository reservationRepository;

  @Autowired
  private UserRepository userRepository;

  private final ScooterRepository scooterRepository;

  @Autowired
  public ScooterService(ScooterRepository scooterRepository) {
    this.scooterRepository = scooterRepository;
  }

  public PagedResponse<Scooter> getAllScooters(int page, int size) {
    validatePageNumberAndSize(page, size);

    Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");
    Page<Scooter> scooters = scooterRepository.findAll(pageable);

    if (scooters.getNumberOfElements() == 0) {
      return new PagedResponse<>(Collections.emptyList(), scooters.getNumber(),
          scooters.getSize(), scooters.getTotalElements(), scooters.getTotalPages(), scooters.isLast());
    }

    return new PagedResponse<>(scooters.getContent(), scooters.getNumber(),
      scooters.getSize(), scooters.getTotalElements(), scooters.getTotalPages(), scooters.isLast());
  }

  public List<Scooter> getAllScooters() {
    return scooterRepository.findAll();
  }

  public Scooter updateScooterLocation(ScooterLocationRequest request) {
    Scooter scooter = scooterRepository.findOneBySerial(request.getSerial());
    Location newLocation = request.getLocation();

    scooter.changeLocation(newLocation);
    scooterRepository.save(scooter);

    return scooter;
  }

  private void validatePageNumberAndSize(int page, int size) {
    if (page < 0) {
      throw new BadRequestException("Page number cannot be less than zero.");
    }

    if (size > AppConstants.MAX_PAGE_SIZE) {
      throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
    }
  }

  @Transactional
  public void returnScooter(UserPrincipal currentUser, ScooterReturnRequest request) {
    Reservation reservation = reservationRepository.findOneById(request.getId());

    Scooter scooter = scooterRepository.findOneById(reservation.getScooter().getId());
    reservation.changeStateToUsed();
    scooter.changeState(ScooterState.AVAILABLE);
    scooter.changeLocation(request.locationToEntity());
  }
}
