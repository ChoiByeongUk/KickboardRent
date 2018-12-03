package com.knu.ssingssing2.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.knu.ssingssing2.model.reservation.ReservationTime;
import java.time.LocalDateTime;

public class ReservationTimeRequest {

  @JsonProperty("start_time")
  private LocalDateTime startTime;

  @JsonProperty("end_time")
  private LocalDateTime endTime;

  public ReservationTime toEntity() {
    return new ReservationTime(startTime, endTime);
  }

}
