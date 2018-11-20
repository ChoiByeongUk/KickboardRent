package com.knu.ssingssing2.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.knu.ssingssing2.model.Location;

public class LocationRequest {

  @JsonProperty
  private double latitude;

  @JsonProperty
  private double longitude;

  @JsonProperty("location_name")
  private String locationName;

  public LocationRequest() {
  }

  public LocationRequest(double latitude, double longitude, String locationName) {
    this.latitude = latitude;
    this.longitude = longitude;
    this.locationName = locationName;
  }

  public Location toEntity() {
    return new Location(latitude, longitude, locationName);
  }


}
