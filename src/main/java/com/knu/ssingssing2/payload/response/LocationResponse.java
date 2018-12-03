package com.knu.ssingssing2.payload.response;

import lombok.Getter;

@Getter
public class LocationResponse {

  private double latitude;

  private double longitude;

  private String location;

  public LocationResponse() {
  }

  public LocationResponse(double latitude, double longitude, String location) {
    this.latitude = latitude;
    this.longitude = longitude;
    this.location = location;
  }
}
