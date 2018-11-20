package com.knu.ssingssing2.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.knu.ssingssing2.model.Location;

public class LocationRequest {

  @JsonProperty
  private double latitude;

  @JsonProperty
  private double longitude;

  @JsonProperty
  private String location;

  public Location toEntity() {
    return new Location(latitude, longitude, location);
  }

}
