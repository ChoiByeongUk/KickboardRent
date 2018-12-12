package com.knu.ssingssing2.payload.response;

import com.knu.ssingssing2.model.reservation.Reservation;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserProfile {

  private Long id;

  private String username;

  private String name;

  private List<Reservation> reservations;

  public void setReservations(List<Reservation> reservations) {
    this.reservations = reservations.stream()
        .filter(r ->
            r.getReservationTime()
                .getStartTime()
                .compareTo(LocalDateTime.now()) >= 0)
        .collect(Collectors.toList());
  }
}
