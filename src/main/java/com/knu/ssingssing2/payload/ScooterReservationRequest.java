package com.knu.ssingssing2.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ScooterReservationRequest {

  @JsonProperty("kickboard_id")
  private Long scooterId;

  @JsonProperty("return_location")
  private LocationRequest returnLocation;

  @JsonProperty("reservation_time")
  private ReservationTimeRequest reservationTime;

  public Long getScooterId() {
    return scooterId;
  }

  public LocationRequest getReturnLocation() {
    return returnLocation;
  }

  public ReservationTimeRequest getReservationTime() {
    return reservationTime;
  }
}
