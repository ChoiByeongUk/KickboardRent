package com.knu.ssingssing2.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.knu.ssingssing2.model.Location;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScooterReturnRequest {

  @JsonProperty("reservation_id")
  private Long id;

  @JsonProperty("location")
  private LocationRequest location;

  public Location locationToEntity() {
    return new Location(location.getLatitude(), location.getLongitude(),
        location.getLocationName());
  }
}
