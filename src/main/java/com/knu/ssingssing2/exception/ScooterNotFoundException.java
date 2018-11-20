package com.knu.ssingssing2.exception;

public class ScooterNotFoundException extends RuntimeException {

  public ScooterNotFoundException() {
    super();
  }

  public ScooterNotFoundException(String message) {
    super(message);
  }

  public ScooterNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

}
