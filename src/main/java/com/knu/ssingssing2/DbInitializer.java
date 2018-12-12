package com.knu.ssingssing2;

import com.knu.ssingssing2.model.Location;
import com.knu.ssingssing2.model.Role;
import com.knu.ssingssing2.model.RoleName;
import com.knu.ssingssing2.model.scooter.Scooter;
import com.knu.ssingssing2.model.scooter.ScooterManufacture;
import com.knu.ssingssing2.model.scooter.ScooterState;
import com.knu.ssingssing2.repository.RoleRepository;
import com.knu.ssingssing2.repository.ScooterManufactureRepository;
import com.knu.ssingssing2.repository.ScooterRepository;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DbInitializer implements CommandLineRunner {

  @Autowired
  private RoleRepository roleRepository;

  private final ScooterRepository scooterRepository;

  private final ScooterManufactureRepository scooterManufactureRepository;

  @Autowired
  public DbInitializer(ScooterRepository scooterRepository,
      ScooterManufactureRepository scooterManufactureRepository) {
    this.scooterRepository = scooterRepository;
    this.scooterManufactureRepository = scooterManufactureRepository;
  }

  @Override
  public void run(String... args) {
    createDummyData();
  }

  private void createDummyData() {
    roleRepository.save(new Role(RoleName.ROLE_USER));
    roleRepository.save(new Role(RoleName.ROLE_ADMIN));

    ScooterManufacture scooterManufacture;

    scooterManufacture = new ScooterManufacture("Xiomi");
    scooterManufactureRepository.save(scooterManufacture);

    for (int i = 1; i <= 20; i++) {
      scooterRepository.save(Scooter.builder()
          .manufacture(scooterManufacture)
          .modelName("Nine Bot")
          .serial(dummySerial(32))
          .state(ScooterState.AVAILABLE)
          .location(new Location(200, 200, "경북대 쪽문"))
          .build());
    }
  }

  private String dummySerial(int length) {
    StringBuilder sb = new StringBuilder();
    Random random = new Random();

    String chars[] = "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z".split(" ");

    for (int i=0 ; i < length; i++) {
      if (i != 0 && i % 8 == 0)
        sb.append("-");
      else
        sb.append(chars[random.nextInt(chars.length)]);

    }

    return sb.toString();
  }
}
