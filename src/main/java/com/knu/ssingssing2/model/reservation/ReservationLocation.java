package com.knu.ssingssing2.model.reservation;

import com.knu.ssingssing2.model.Location;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationLocation {

  private Location rentalLocation;

  private Location returnLocation;

}
