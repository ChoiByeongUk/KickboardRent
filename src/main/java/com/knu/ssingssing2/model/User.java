package com.knu.ssingssing2.model;

import com.knu.ssingssing2.model.reservation.Reservation;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

  @Id
  @GeneratedValue
  private Long id;

  private String name;

  @OneToMany
  private List<Reservation> reservations = new ArrayList<>();

}
