package com.knu.ssingssing2.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.knu.ssingssing2.model.scooter.Location;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScooterResponse {

  @JsonProperty("kickboard_id")
  private Long id;

  @JsonProperty("kickboard_manufacture")
  private String manufacture;

  @JsonProperty("kickboard_modelname")
  private String modelName;

  @JsonProperty("kickboard_serial")
  private String serial;

  @JsonProperty("kickboard_state")
  private String state;

  @JsonProperty("kickboard_location")
  private Location location;

}
