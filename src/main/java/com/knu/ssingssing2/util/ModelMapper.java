package com.knu.ssingssing2.util;

import com.knu.ssingssing2.model.scooter.Scooter;
import com.knu.ssingssing2.payload.response.ScooterResponse;

public class ModelMapper {

  public static ScooterResponse mapScooterToScooterResponse(Scooter scooter) {
    ScooterResponse scooterResponse = new ScooterResponse();
    scooterResponse.setId(scooter.getId());
    scooterResponse.setManufacture(scooter.getManufacture().getName());
    scooterResponse.setModelName(scooter.getModelName());
    scooterResponse.setSerial(scooter.getSerial());
    scooterResponse.setState(scooter.getState().toString());
    scooterResponse.setLocation(scooter.getLocation());

    return scooterResponse;
  }
}
