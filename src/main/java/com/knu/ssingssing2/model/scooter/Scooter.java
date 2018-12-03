package com.knu.ssingssing2.model.scooter;

import com.knu.ssingssing2.model.Location;
import com.knu.ssingssing2.model.reservation.Reservation;
import com.knu.ssingssing2.model.reservation.ReservationTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Scooter {

  @Id
  @GeneratedValue
  private Long id;

  @OneToOne
  private ScooterManufacture manufacture;

  private String modelName;

  private String serial;

  @Enumerated(EnumType.STRING)
  private ScooterState state;

  private Location location;

  @OneToMany(
      mappedBy = "scooter",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true
  )
  private List<Reservation> reservations = new ArrayList<>();

  public Scooter(ScooterManufacture manufacture, String modelName, String serial) {
    this.manufacture = manufacture;
    this.modelName = modelName;
    this.serial = serial;
    this.state = ScooterState.AVAILABLE;
    this.location = new Location(0, 0, null);
  }

  public boolean isAvailable() {
    return state.isAvailable();
  }

  public void changeState(ScooterState state) {
    this.state = state;
  }

  public void changeLocation(Location location) {
    this.location = location;
  }

  public boolean isAvailableReservation(ReservationTime time) {
    return !isReserved(time);
  }

  public boolean isReserved(ReservationTime time) {
    return reservations.stream()
        .anyMatch(reservation -> reservation.isReserved(time));
  }

  public void addReservation(Reservation reservation) {
    this.reservations.add(reservation);
  }

}