package com.knu.ssingssing2.exception;

public class ReservationNotFoundException extends RuntimeException {

  public ReservationNotFoundException() {
    super();
  }

  public ReservationNotFoundException(String message) {
    super(message);
  }

  public ReservationNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

}
