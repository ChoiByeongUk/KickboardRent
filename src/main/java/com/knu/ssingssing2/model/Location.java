package com.knu.ssingssing2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class Location {

  @JsonProperty
  private double latitude;

  @JsonProperty
  private double longitude;

  @JsonProperty("location_name")
  private String location;

  @Override
  public boolean equals(Object o) {
    return o.equals(location);
  }

  @Builder
  public Location(double latitude, double longitude, String location) {
    this.latitude = latitude;
    this.longitude = longitude;
    this.location = location;
  }
}
