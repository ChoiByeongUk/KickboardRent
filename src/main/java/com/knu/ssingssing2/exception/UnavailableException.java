package com.knu.ssingssing2.exception;

public class UnavailableException extends RuntimeException {

  public UnavailableException(String message) {
    super(message);
  }

  public UnavailableException(String message, Throwable cause) {
    super(message, cause);
  }

}
