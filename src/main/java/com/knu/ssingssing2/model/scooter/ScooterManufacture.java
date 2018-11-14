package com.knu.ssingssing2.model.scooter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "SCOOTER_MANUFACTURE")
public class ScooterManufacture {

  @Id
  @GeneratedValue
  private Long id;

  private String name;

  public ScooterManufacture() { }

  public ScooterManufacture(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
