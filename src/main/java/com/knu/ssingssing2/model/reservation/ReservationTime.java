package com.knu.ssingssing2.model.reservation;

import java.time.LocalDateTime;
import javax.persistence.Embeddable;

@Embeddable
public class ReservationTime {

  private LocalDateTime startTime;

  private LocalDateTime endTime;

  public ReservationTime() {
  }

  public ReservationTime(LocalDateTime startTime, LocalDateTime endTime) {
    this.startTime = startTime;
    this.endTime = endTime;
  }

  public LocalDateTime getStartTime() {
    return startTime;
  }

  public LocalDateTime getEndTime() {
    return endTime;
  }

  public boolean isDuplicated(ReservationTime time) {
    return this.endTime.compareTo(time.getStartTime()) >= 0 &&
        this.startTime.compareTo(time.getEndTime()) <= 0;
  }


}
