package com.knu.ssingssing2.model.scooter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity(name = "SCOOTER_MANUFACTURE")
@NoArgsConstructor
public class ScooterManufacture {

  @Id
  @GeneratedValue
  private Long id;

  private String name;

  @Builder
  public ScooterManufacture(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
