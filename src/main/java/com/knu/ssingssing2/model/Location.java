package com.knu.ssingssing2.model;

import javax.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class Location {

  private double latitude;

  private double longitude;

  private String location;

  public Location() {
  }

  public Location(double latitude, double longitude, String location) {
    this.latitude = latitude;
    this.longitude = longitude;
    this.location = location;
  }

  @Override
  public boolean equals(Object o) {
    return o.equals(location);
  }

}
