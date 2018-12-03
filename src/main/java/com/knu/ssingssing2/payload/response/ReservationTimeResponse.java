package com.knu.ssingssing2.payload.response;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ReservationTimeResponse {

  private LocalDateTime startTime;

  private LocalDateTime endTime;

  public ReservationTimeResponse() {
  }

  public ReservationTimeResponse(LocalDateTime startTime, LocalDateTime endTime) {
    this.startTime = startTime;
    this.endTime = endTime;
  }
}
