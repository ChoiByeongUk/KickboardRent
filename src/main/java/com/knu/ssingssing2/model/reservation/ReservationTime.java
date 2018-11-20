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

  public boolean isDuplicated(LocalDateTime now) {
    return now.compareTo(startTime) >= 0 && now.compareTo(endTime) <= 0;
  }

  public boolean isDuplicated(ReservationTime time) {
    LocalDateTime start = time.getStartTime();
    LocalDateTime end = time.getEndTime();

    return isDuplicated(start) || isDuplicated(end);
  }


}
