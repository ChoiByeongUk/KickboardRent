package com.knu.ssingssing2.model.reservation;

import com.knu.ssingssing2.model.Location;
import javax.persistence.Embeddable;

@Embeddable
public class ReservationLocation {

  private Location rentalLocation;

  private Location returnLocation;

  public ReservationLocation() {
  }

  public ReservationLocation(Location rentalLocation,
      Location returnLocation) {
    this.rentalLocation = rentalLocation;
    this.returnLocation = returnLocation;
  }

  public Location getRentalLocation() {
    return rentalLocation;
  }

  public Location getReturnLocation() {
    return returnLocation;
  }
}
