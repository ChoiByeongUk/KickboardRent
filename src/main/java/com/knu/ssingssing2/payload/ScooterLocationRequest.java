package com.knu.ssingssing2.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ScooterLocationRequest {

  @NotBlank
  private String serial;

  @NotNull
  private LocationRequest location;

  public ScooterLocationRequest() {
  }

  public ScooterLocationRequest(String serial, LocationRequest location) {
    this.serial = serial;
    this.location = location;
  }

  public String getSerial() {
    return serial;
  }

  public LocationRequest getLocation() {
    return location;
  }
}
