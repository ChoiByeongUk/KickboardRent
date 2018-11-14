package com.knu.ssingssing2.model.scooter;

import java.util.Objects;
import javax.persistence.Embeddable;

@Embeddable
public class Location {

  private double latitude;

  private double longitude;

  public Location() { }

  public Location(double latitude, double longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Location location = (Location) o;
    return Double.compare(location.latitude, latitude) == 0 &&
        Double.compare(location.longitude, longitude) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(latitude, longitude);
  }
}
