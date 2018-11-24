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
        LocalDateTime.parse("2018-11-11T12:00:00.00")
    );
  }

  @Test
  public void isDuplicatedBetween9To12And8To11() {
    LocalDateTime start = LocalDateTime.parse("2018-11-11T08:00:00.00");
    LocalDateTime end = LocalDateTime.parse("2018-11-11T14:00:00.00");
    assertThat(reservationTime.isDuplicated(new ReservationTime(start, end)), is(true));
  }

  @Test
  public void isDuplicatedBetween9To12And8To10() {
    LocalDateTime start = LocalDateTime.parse("2018-11-11T08:00:00.00");
    LocalDateTime end = LocalDateTime.parse("2018-11-11T10:00:00.00");
    assertThat(reservationTime.isDuplicated(new ReservationTime(start, end)), is(true));
  }

  @Test
  public void isDuplicatedBetween9To12And7To8() {
    LocalDateTime start = LocalDateTime.parse("2018-11-11T10:00:00.00");
    LocalDateTime end = LocalDateTime.parse("2018-11-11T15:00:00.00");
    assertThat(reservationTime.isDuplicated(new ReservationTime(start, end)), is(true));
  }

  @Test
  public void isDuplicatedBetween9To12And10To11() {
    LocalDateTime start = LocalDateTime.parse("2018-11-11T10:00:00.00");
    LocalDateTime end = LocalDateTime.parse("2018-11-11T11:00:00.00");
    assertThat(reservationTime.isDuplicated(new ReservationTime(start, end)), is(true));
  }

  @Test
  public void isDuplicatedBetween9To12And8To9() {
    LocalDateTime start = LocalDateTime.parse("2018-11-11T08:00:00.00");
    LocalDateTime end = LocalDateTime.parse("2018-11-11T09:00:00.00");
    assertThat(reservationTime.isDuplicated(new ReservationTime(start, end)), is(true));
  }

  @Test
  public void isDuplicatedBetween9To12And13To14() {
    LocalDateTime start = LocalDateTime.parse("2018-11-11T13:00:00.00");
    LocalDateTime end = LocalDateTime.parse("2018-11-11T14:00:00.00");
    assertThat(reservationTime.isDuplicated(new ReservationTime(start, end)), is(false));
  }

}
