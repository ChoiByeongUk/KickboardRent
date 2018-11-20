package com.knu.ssingssing2.repository;

import com.knu.ssingssing2.model.scooter.Scooter;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScooterRepository extends JpaRepository<Scooter, String> {

  Scooter findOneById(Long id);

  Scooter findOneBySerial(String serial);

  List<Scooter> findAllByModelName(String modelName);
}
