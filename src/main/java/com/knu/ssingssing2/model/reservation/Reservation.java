package com.knu.ssingssing2.model.reservation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.knu.ssingssing2.exception.UnavailableException;
import com.knu.ssingssing2.model.Location;
import com.knu.ssingssing2.model.scooter.Scooter;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne(cascade = CascadeType.ALL, optional = false)
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

  @JsonProperty("reservation_time")
  private ReservationTime reservationTime;

  @JsonProperty("reservation_state")
  @Enumerated(EnumType.STRING)
  private ReservationState reservationState;

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

  public void changeStateToCanceled() {
    this.reservationState = ReservationState.CANCELED;
  }

  public void changeStateToUsed() {
    this.reservationState = ReservationState.USED;
  }

  public boolean isReserved(ReservationTime time) {
    return isReserved() && isDuplicatedTime(time);
  }

  private boolean isReserved() {
    return reservationState.equals(ReservationState.RESERVED);
  }

  private boolean isDuplicatedTime(ReservationTime time) {
    return reservationTime.isDuplicated(time);
  }

}
