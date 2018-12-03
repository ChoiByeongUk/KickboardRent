package com.knu.ssingssing2.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.knu.ssingssing2.model.reservation.ReservationState;
import lombok.Getter;

@Getter
public class ReservationResponse {

  private Long id;

  private ScooterResponse scooter;

  private ReservationLocationResponse location;

  @JsonProperty("reservation_time")
  private ReservationTimeResponse reservationTime;

  @JsonProperty("reservation_state")
  private ReservationState reservationState;

  public ReservationResponse() {
  }

  public ReservationResponse(Long id, ScooterResponse scooter,
      ReservationLocationResponse location,
      ReservationTimeResponse reservationTime,
      ReservationState reservationState) {
    this.id = id;
    this.scooter = scooter;
    this.location = location;
    this.reservationTime = reservationTime;
    this.reservationState = reservationState;
  }
}
