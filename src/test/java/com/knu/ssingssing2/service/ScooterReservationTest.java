package com.knu.ssingssing2.service;

import static com.knu.ssingssing2.Utils.dummySerial;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import com.knu.ssingssing2.exception.UnavailableException;
import com.knu.ssingssing2.model.Location;
import com.knu.ssingssing2.model.reservation.ReservationTime;
import com.knu.ssingssing2.model.scooter.Scooter;
import com.knu.ssingssing2.model.scooter.ScooterManufacture;
import com.knu.ssingssing2.model.scooter.ScooterState;
import com.knu.ssingssing2.payload.ApiResponse;
import com.knu.ssingssing2.payload.ScooterResponse;
import com.knu.ssingssing2.repository.ReservationRepository;
import com.knu.ssingssing2.repository.ScooterManufactureRepository;
import com.knu.ssingssing2.repository.ScooterRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ScooterReservationTest {

  @Autowired
  private ReservationService reservationService;

  @Autowired
  private ScooterRepository scooterRepository;

  @Autowired
  private ScooterManufactureRepository scooterManufactureRepository;

  @Autowired
  private ReservationRepository reservationRepository;

  private Scooter scooter;

  @Before
  public void destroy() {
    reservationRepository.deleteAll();
    scooterRepository.deleteAll();
    scooterManufactureRepository.deleteAll();
  }

  @Test
  public void successScooterReservation() {
    createDummyData();
    scooter = scooterRepository.findOneBySerial("12345678-12345678-12345678-12345678");

    Location returnLocation = new Location(200, 200, "경북대 쪽문");
    LocalDateTime start = LocalDateTime.parse("2018-11-11T11:00:00.00");
    LocalDateTime end = LocalDateTime.parse("2018-11-11T14:00:00.00");
    ReservationTime reservationTime = new ReservationTime(start, end);

    ApiResponse response = reservationService
        .reservation(scooter.getId(), returnLocation, reservationTime);
    assertThat(response.getSuccess(), is(true));
  }

  @Test(expected = UnavailableException.class)
  public void canNotReserveScooterIsAlreadyReserved() {
    createDummyData();
    scooter = scooterRepository.findOneBySerial("12345678-12345678-12345678-12345678");

    specificScooterReserved();

    LocalDateTime start = LocalDateTime.parse("2018-11-11T12:00:00.00");
    LocalDateTime end = LocalDateTime.parse("2018-11-11T13:00:00.00");
    Location returnLocation = new Location(200, 200, "경북대 쪽문");
    ReservationTime reservationTime = new ReservationTime(start, end);

    reservationService.reservation(scooter.getId(), returnLocation, reservationTime);
  }

  @Test
  public void findAllAvailableReservationScooterListsWithModelAndLocationAndCurrentTime() {
    createReservationDummyData();
    String rentalLocation = "경북대 쪽문";
    LocalDateTime start = LocalDateTime.parse("2018-11-11T11:00:00.00");
    LocalDateTime end = LocalDateTime.parse("2018-11-11T14:30:00.00");
    ReservationTime time = new ReservationTime(start, end);

    List<ScooterResponse> scooters = reservationService
        .findAllAvailableReservationScootersWithModelAndLocation("Nine Bot", rentalLocation, time);
    assertThat(scooters.size(), is(7));
  }

  @Test
  public void canNotFindAvailableReservationScooterListsWithModelAndLocationAndCurrentTime() {
    createReservationDummyData();
    String rentalLocation = "경북대 북문";
    LocalDateTime start = LocalDateTime.parse("2018-11-11T11:00:00.00");
    LocalDateTime end = LocalDateTime.parse("2018-11-11T14:30:00.00");
    ReservationTime time = new ReservationTime(start, end);

    List<ScooterResponse> scooters = reservationService
        .findAllAvailableReservationScootersWithModelAndLocation("Nine Bot", rentalLocation, time);
    assertThat(scooters.size(), is(0));
  }

  @Test
  public void findAllAvailableReservationScooterListsWithLocationAndCurrentTime() {
    createReservationDummyData();
    ScooterManufacture scooterManufacture;
    scooterManufacture = new ScooterManufacture("Samsung");
    scooterManufactureRepository.save(scooterManufacture);

    scooterRepository.save(
        Scooter.builder()
            .manufacture(scooterManufacture)
            .modelName("Galaxy")
            .serial(dummySerial(32))
            .state(ScooterState.AVAILABLE)
            .location(new Location(200, 200, "경북대 쪽문"))
            .build()
    );

    String rentalLocation = "경북대 쪽문";
    LocalDateTime start = LocalDateTime.parse("2018-11-11T11:00:00.00");
    LocalDateTime end = LocalDateTime.parse("2018-11-11T14:30:00.00");
    ReservationTime time = new ReservationTime(start, end);

    List<ScooterResponse> scooters = reservationService
        .findAllAvailableReservationScootersWithLocation(rentalLocation, time);
    assertThat(scooters.size(), is(8));
  }

  @Test
  public void findAllAvailableReservationScooterListsWithNullLocationAndCurrentTime() {
    createReservationDummyData();
    ScooterManufacture scooterManufacture;
    scooterManufacture = new ScooterManufacture("Samsung");
    scooterManufactureRepository.save(scooterManufacture);

    scooterRepository.save(
        Scooter.builder()
            .manufacture(scooterManufacture)
            .modelName("Galaxy")
            .serial(dummySerial(32))
            .state(ScooterState.AVAILABLE)
            .location(new Location(200, 200, "경북대 북문"))
            .build()
    );

    LocalDateTime start = LocalDateTime.parse("2018-11-11T11:00:00.00");
    LocalDateTime end = LocalDateTime.parse("2018-11-11T14:30:00.00");
    ReservationTime time = new ReservationTime(start, end);

    List<ScooterResponse> scooters = reservationService
        .findAllAvailableReservationScootersWithLocation(null, time);
    assertThat(scooters.size(), is(8));
  }

  private void createReservationDummyData() {
    ScooterManufacture scooterManufacture;
    scooterManufacture = new ScooterManufacture("Xiomi");
    scooterManufactureRepository.save(scooterManufacture);
    List<Scooter> scooters = new ArrayList<>();

    for (int i = 1; i <= 10; i++) {
      scooters.add(scooterRepository.save(
          Scooter.builder()
              .manufacture(scooterManufacture)
              .modelName("Nine Bot")
              .serial(dummySerial(32))
              .state(ScooterState.AVAILABLE)
              .location(new Location(200, 200, "경북대 쪽문"))
              .build()
      ));
    }

    LocalDateTime start, end;
    ReservationTime reservationTime;
    Location returnLocation = new Location(200, 200, "경북대 쪽문");
    start = LocalDateTime.parse("2018-11-11T11:00:00.00");
    end = LocalDateTime.parse("2018-11-11T14:00:00.00");
    reservationTime = new ReservationTime(start, end);
    reservationService.reservation(scooters.get(0).getId(), returnLocation, reservationTime);
    reservationService.reservation(scooters.get(1).getId(), returnLocation, reservationTime);
    reservationService.reservation(scooters.get(2).getId(), returnLocation, reservationTime);
    start = LocalDateTime.parse("2018-11-11T12:00:00.00");
    end = LocalDateTime.parse("2018-11-11T14:00:00.00");
    reservationTime = new ReservationTime(start, end);
    reservationService.reservation(scooters.get(3).getId(), returnLocation, reservationTime);
  }

  private void specificScooterReserved() {
    LocalDateTime start, end;
    ReservationTime reservationTime;
    Location returnLocation = new Location(200, 200, "경북대 쪽문");
    start = LocalDateTime.parse("2018-11-11T11:00:00.00");
    end = LocalDateTime.parse("2018-11-11T14:00:00.00");
    reservationTime = new ReservationTime(start, end);
    reservationService.reservation(scooter.getId(), returnLocation, reservationTime);
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

    scooterRepository.save(
        Scooter.builder()
            .manufacture(scooterManufacture)
            .modelName("Nine Bot Series " + 999)
            .serial("12345678-12345678-12345678-12345678")
            .state(ScooterState.AVAILABLE)
            .location(new Location(200, 200, "경북대 쪽문"))
            .build()
    );
  }

}
