package com.knu.ssingssing2.model.reservation;

import com.knu.ssingssing2.exception.UnavailableException;
import com.knu.ssingssing2.model.Location;
import com.knu.ssingssing2.model.scooter.Scooter;
import java.time.LocalDateTime;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Builder
public class Reservation {

  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "scooter_id", nullable = false)
  private Scooter scooter;

  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "rentalLocation.latitude",
          column = @Column(name = "RENTAL_LATITUDE")),
      @AttributeOverride(name = "rentalLocation.longitude",
          column = @Column(name = "RENTAL_LONGITUDE")),
      @AttributeOverride(name = "rentalLocation.location",
          column = @Column(name = "RENTAL_LOCATION")),
      @AttributeOverride(name = "returnLocation.latitude",
          column = @Column(name = "RETURN_LATITUDE")),
      @AttributeOverride(name = "returnLocation.longitude",
          column = @Column(name = "RETURN_LONGITUDE")),
      @AttributeOverride(name = "returnLocation.location",
          column = @Column(name = "RETURN_LOCATION")),
  })
  private ReservationLocation location;

  private ReservationTime reservationTime;

  public Reservation() {
  }

  public Reservation(Long id, Scooter scooter,
      ReservationLocation location,
      ReservationTime reservationTime) {
    this.id = id;
    this.scooter = scooter;
    this.location = location;
    this.reservationTime = reservationTime;
  }

  public void changeReturnLocation(Location returnLocation) {
    Location rentalLocation = this.location.getRentalLocation();
    this.location = new ReservationLocation(rentalLocation, returnLocation);
  }

  private void changeRentalLocation(Location rentalLocation) {
    Location returnLocation = this.location.getReturnLocation();
    this.location = new ReservationLocation(rentalLocation, returnLocation);
  }

  public void changeScooter(Scooter scooter) {
    if (!scooter.isAvailable()) {
      throw new UnavailableException("The selected model is not currently available.");
    }

    this.scooter = scooter;
    changeRentalLocation(scooter.getLocation());
  }

  public boolean isDuplicatedTime(LocalDateTime time) {
    return reservationTime.isDuplicated(time);
  }

  public boolean isDuplicatedTime(ReservationTime time) {
    return reservationTime.isDuplicated(time);
  }

}
