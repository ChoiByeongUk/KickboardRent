package com.knu.ssingssing2.model;

import com.knu.ssingssing2.model.reservation.Reservation;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserReservation {

  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne
  private User user;

  @ManyToOne
  private Reservation reservation;

  private LocalDateTime startTime;

  private LocalDateTime endTime;

}
