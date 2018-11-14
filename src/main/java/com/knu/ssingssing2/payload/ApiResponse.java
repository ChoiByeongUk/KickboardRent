package com.knu.ssingssing2.payload;

public class ApiResponse {

  private Boolean success;
  private String message;

  public Boolean getSuccess() {
    return success;
  }

  public String getMessage() {
    return message;
  }

  public ApiResponse(Boolean success, String message) {

    this.success = success;
    this.message = message;
  }

}
