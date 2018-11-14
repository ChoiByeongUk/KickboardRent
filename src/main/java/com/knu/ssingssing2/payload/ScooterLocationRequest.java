package com.knu.ssingssing2.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ScooterLocationRequest {

  @NotBlank
  private String serial;

  @NotNull
  private Location location;

  public ScooterLocationRequest() {
  }

  public ScooterLocationRequest(@NotBlank String serial,
      @NotNull Location location) {
    this.serial = serial;
    this.location = location;
  }

  public String getSerial() {
    return serial;
  }

  public Location getLocation() {
    return location;
  }
}
