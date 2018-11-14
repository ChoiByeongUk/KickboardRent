package com.knu.ssingssing2.model;

import com.knu.ssingssing2.model.scooter.Location;
import com.knu.ssingssing2.model.scooter.Scooter;
import com.knu.ssingssing2.model.scooter.ScooterManufacture;
import com.knu.ssingssing2.model.scooter.ScooterState;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

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
    scooter.changeLocation(new Location(35.886233, 128.609141));
    assertThat(scooter.getLocation(), is(new Location(35.886233, 128.609141)));
    scooter.changeLocation(new Location(35.892308, 128.610877));
    assertThat(scooter.getLocation(), not(new Location(35.886233, 128.609141)));
  }
}
