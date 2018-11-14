package com.knu.ssingssing2.model.scooter;

public enum ScooterState {

  AVAILABLE {
    public boolean isAvailable() {
      return true;
    }
  }, BROKEN, REPAIR;

  public boolean isAvailable() {
    return false;
  }

}