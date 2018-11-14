package com.knu.ssingssing2.model.scooter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.Getter;

@Getter
@Entity
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

  public Scooter() { }

  public Scooter(ScooterManufacture manufacture, String modelName, String serial) {
    this.manufacture = manufacture;
    this.modelName = modelName;
    this.serial = serial;
    this.state = ScooterState.AVAILABLE;
    this.location = new Location(0, 0);
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

}