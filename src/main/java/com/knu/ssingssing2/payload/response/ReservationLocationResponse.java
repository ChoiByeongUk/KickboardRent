package com.knu.ssingssing2.payload.response;

import lombok.Getter;

@Getter
public class ReservationLocationResponse {

  private LocationResponse rentalLocation;

  private LocationResponse returnLocation;

  public ReservationLocationResponse() {
  }

  public ReservationLocationResponse(LocationResponse rentalLocation,
      LocationResponse returnLocation) {
    this.rentalLocation = rentalLocation;
    this.returnLocation = returnLocation;
  }
}
