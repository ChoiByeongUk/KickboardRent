package com.knu.ssingssing2.service;

import static com.knu.ssingssing2.Utils.dummySerial;

import com.knu.ssingssing2.model.Location;
import com.knu.ssingssing2.model.scooter.Scooter;
import com.knu.ssingssing2.model.scooter.ScooterManufacture;
import com.knu.ssingssing2.model.scooter.ScooterState;
import com.knu.ssingssing2.repository.ScooterManufactureRepository;
import com.knu.ssingssing2.repository.ScooterRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ScooterServiceTest {

  @Autowired
  private ScooterService scooterService;

  @Autowired
  private ScooterRepository scooterRepository;

  @Autowired
  private ScooterManufactureRepository scooterManufactureRepository;

  @Before
  public void init() {
    createDummyData();
  }

  @Test
  public void getAllScootersWithPaging() {
    scooterService.getAllScooters(0, 10);
  }

  @Test
  public void getAllScootersWithoutPaging() {
    scooterService.getAllScooters();
  }

  private void createDummyData() {
    ScooterManufacture scooterManufacture;
    scooterManufacture = new ScooterManufacture("Xiomi");
    scooterManufactureRepository.save(scooterManufacture);

    for (int i = 1; i <= 100; i++) {
      scooterRepository.save(
          Scooter.builder()
              .manufacture(scooterManufacture)
              .modelName("Nine Bot Series " + i)
              .serial(dummySerial(32))
              .state(ScooterState.AVAILABLE)
              .location(new Location(200, 200, "경북대 쪽문"))
              .build()
      );
    }
  }

}
