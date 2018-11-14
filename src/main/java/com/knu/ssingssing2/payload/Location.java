package com.knu.ssingssing2.payload;

import javax.validation.constraints.NotNull;

public class Location {

  @NotNull
  private Double latitude;

  @NotNull
  private Double longitude;

  public Location() {
  }

  public Location(@NotNull Double latitude,
      @NotNull Double longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public Double getLatitude() {
    return latitude;
  }

  public Double getLongitude() {
    return longitude;
  }
}
