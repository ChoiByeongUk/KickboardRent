package com.knu.ssingssing2;

import com.knu.ssingssing2.model.scooter.Scooter;
import com.knu.ssingssing2.model.scooter.ScooterManufacture;
import com.knu.ssingssing2.repository.ScooterManufactureRepository;
import com.knu.ssingssing2.repository.ScooterRepository;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DbInitializer implements CommandLineRunner {

  @Autowired
  private ScooterRepository scooterRepository;

  @Autowired
  private ScooterManufactureRepository scooterManufactureRepository;

  @Override
  public void run(String... args) throws Exception {
    createDummyData();
  }

  private void createDummyData() {
    ScooterManufacture scooterManufacture;

    scooterManufacture = new ScooterManufacture("Xiomi");
    scooterManufactureRepository.save(scooterManufacture);

    for (int i = 1; i <= 100; i++) {
      scooterRepository.save(new Scooter(scooterManufacture, "Nine Bot Series " + i, dummySerial(32)));
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
