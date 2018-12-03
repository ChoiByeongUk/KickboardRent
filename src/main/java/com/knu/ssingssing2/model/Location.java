package com.knu.ssingssing2.model;

import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Location {

  private double latitude;

  private double longitude;

  private String location;

  @Override
  public boolean equals(Object o) {
    return o.equals(location);
  }

}
