package com.knu.ssingssing2.service;

import static com.knu.ssingssing2.Utils.dummySerial;
import static org.assertj.core.api.Assertions.assertThat;

import com.knu.ssingssing2.model.Location;
import com.knu.ssingssing2.model.scooter.Scooter;
import com.knu.ssingssing2.model.scooter.ScooterManufacture;
import com.knu.ssingssing2.model.scooter.ScooterState;
import com.knu.ssingssing2.payload.request.ScooterLocationRequest;
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

  @Test
  public void updateScooterLocation() {
    ScooterManufacture manufacture = new ScooterManufacture().builder().name("Samsung").build();
    scooterManufactureRepository.save(manufacture);

    Scooter scooter = new Scooter().builder()
        .manufacture(manufacture)
        .serial("ABCDEFG-ABCDEFG-ABCDEFG")
        .modelName("Galaxy")
        .location(new Location().builder()
            .latitude(123.123)
            .longitude(123.123)
            .location("KNU")
            .build())
        .build();

    scooterRepository.save(scooter);
    ScooterLocationRequest request =
        new ScooterLocationRequest("ABCDEFG-ABCDEFG-ABCDEFG",
            new Location().builder()
                .latitude(111.111)
                .longitude(111.111)
                .location("SNU")
                .build()
        );
    scooterService.updateScooterLocation(request);

    assertThat(scooterRepository.findOneBySerial("ABCDEFG-ABCDEFG-ABCDEFG")
        .getLocation()
        .getLocation()).isEqualTo("SNU");
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
