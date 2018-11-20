package com.knu.ssingssing2.api;

import com.knu.ssingssing2.model.reservation.ReservationTime;
import com.knu.ssingssing2.payload.ApiResponse;
import com.knu.ssingssing2.payload.ScooterReservationRequest;
import com.knu.ssingssing2.payload.ScooterResponse;
import com.knu.ssingssing2.service.ReservationService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

  private final ReservationService reservationService;

  @Autowired
  public ReservationController(ReservationService reservationService) {
    this.reservationService = reservationService;
  }

  @GetMapping("/{modelName}")
  public ResponseEntity<?> getAllAvailableReservationScooters(
      @PathVariable(name = "modelName") String modelName,
      @RequestParam(name = "location", required = false) String location,
      @RequestParam(name = "start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
      @RequestParam(name = "end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

    List<ScooterResponse> responses;
    ReservationTime time = new ReservationTime(start, end);

    responses = reservationService.findAllAvailableReservationScootersWithModelAndLocation(
        modelName, location, time);

    if (responses.size() == 0) {
      return new ResponseEntity<>(
          new ApiResponse(false, "can not find available scooters"),
          HttpStatus.OK);
    }
    return new ResponseEntity<>(responses, HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<?> getAllAvailableReservationScooters(
      @RequestParam(name = "location", required = false) String location,
      @RequestParam(name = "start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
      @RequestParam(name = "end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
  ) {

    List<ScooterResponse> responses;
    ReservationTime time = new ReservationTime(start, end);

    responses = reservationService.findAllAvailableReservationScootersWithLocation(location, time);

    if (responses.size() == 0) {
      return new ResponseEntity<>(
          new ApiResponse(false, "can not find available scooters"),
          HttpStatus.OK);
    }
    return new ResponseEntity<>(responses, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<ApiResponse> reservationScooter(
      @RequestBody ScooterReservationRequest request) {
    ApiResponse apiResponse = reservationService.reservation(request.getScooterId(),
        request.getReturnLocation().toEntity(), request.getReservationTime().toEntity());

    return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
  }

}
