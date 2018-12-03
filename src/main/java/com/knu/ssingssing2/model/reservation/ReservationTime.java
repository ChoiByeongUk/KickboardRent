package com.knu.ssingssing2.model.reservation;

import java.time.LocalDateTime;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationTime {

  private LocalDateTime startTime;

  private LocalDateTime endTime;

  public boolean isDuplicated(ReservationTime time) {
    return this.endTime.compareTo(time.getStartTime()) >= 0 &&
        this.startTime.compareTo(time.getEndTime()) <= 0;
  }

}
