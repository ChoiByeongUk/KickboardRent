package com.knu.ssingssing2.model.reservation;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;

public class ReservationTimeTest {

  private ReservationTime reservationTime;

  @Before
  public void init() {
    reservationTime = new ReservationTime(
        LocalDateTime.parse("2018-11-11T09:00:00.00"),
        LocalDateTime.parse("2018-11-11T10:00:00.00")
    );
  }

  @Test
  public void isDuplicatedNowBetween9And10() {
    LocalDateTime now = LocalDateTime.parse("2018-11-11T09:10:00.00");
    assertThat(reservationTime.isDuplicated(now), is(true));
  }

  @Test
  public void isNotDuplicatedNowBetween9And10() {
    LocalDateTime now = LocalDateTime.parse("2018-11-11T11:00:00.00");
    assertThat(reservationTime.isDuplicated(now), is(false));
  }

}
