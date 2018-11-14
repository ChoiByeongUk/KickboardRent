package com.knu.ssingssing2.repository;

import com.knu.ssingssing2.model.scooter.Scooter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScooterRepository extends JpaRepository<Scooter, String> {

  Scooter findOneBySerial(String serial);

}
