package com.knu.ssingssing2.model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import com.knu.ssingssing2.model.scooter.Scooter;
import com.knu.ssingssing2.model.scooter.ScooterManufacture;
import com.knu.ssingssing2.model.scooter.ScooterState;
import org.junit.Before;
import org.junit.Test;

public class ScooterTest {

  private Scooter scooter;

  @Before
  public void init() {
    ScooterManufacture manufacture = new ScooterManufacture("Xiaomi");
    scooter = new Scooter(manufacture, "nine bot", "ABCDEFG-BFDSA");
  }

  @Test
  public void scooterConstructorStateIsAvailableTest() {
    assertThat(scooter.isAvailable(), is(true));
  }

  @Test
  public void changeStateTest() {
    scooter.changeState(ScooterState.BROKEN);
    assertThat(scooter.isAvailable(), is(false));
  }

  @Test
  public void changeLocation() {
    scooter.changeLocation(new Location(35.886233, 128.609141, "경북대 정문"));
    assertThat(scooter.getLocation(), is(new Location(35.886233, 128.609141, "경북대 정문")));
  }
}
