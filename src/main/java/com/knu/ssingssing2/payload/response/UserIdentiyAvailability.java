package com.knu.ssingssing2.payload.response;

public class UserIdentiyAvailability {

  private Boolean available;

  public UserIdentiyAvailability(Boolean available) {
    this.available = available;
  }

  public Boolean getAvailable() {
    return available;
  }

  public void setAvailable(Boolean available) {
    this.available = available;
  }
}
