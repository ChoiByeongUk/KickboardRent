package com.knu.ssingssing2.service;

import com.knu.ssingssing2.exception.BadRequestException;
import com.knu.ssingssing2.model.Location;
import com.knu.ssingssing2.model.scooter.Scooter;
import com.knu.ssingssing2.payload.PagedResponse;
import com.knu.ssingssing2.payload.ScooterLocationRequest;
import com.knu.ssingssing2.payload.ScooterResponse;
import com.knu.ssingssing2.repository.ScooterRepository;
import com.knu.ssingssing2.util.AppConstants;
import com.knu.ssingssing2.util.ModelMapper;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ScooterService {

  private final ScooterRepository scooterRepository;

  @Autowired
  public ScooterService(ScooterRepository scooterRepository) {
    this.scooterRepository = scooterRepository;
  }

  public PagedResponse<ScooterResponse> getAllScooters(int page, int size) {
    validatePageNumberAndSize(page, size);

    Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");
    Page<Scooter> scooters = scooterRepository.findAll(pageable);

    if (scooters.getNumberOfElements() == 0) {
      return new PagedResponse<>(Collections.emptyList(), scooters.getNumber(),
          scooters.getSize(), scooters.getTotalElements(), scooters.getTotalPages(), scooters.isLast());
    }

    List<ScooterResponse> scooterResponses = scooters.map(ModelMapper::mapScooterToScooterResponse)
        .getContent();

    return new PagedResponse<>(scooterResponses, scooters.getNumber(),
      scooters.getSize(), scooters.getTotalElements(), scooters.getTotalPages(), scooters.isLast());
  }

  public List<ScooterResponse> getAllScooters() {
    List<Scooter> scooters = scooterRepository.findAll();
    return scooters.stream().map(
        ModelMapper::mapScooterToScooterResponse).collect(Collectors.toList());
  }

  public ScooterResponse updateScooterLocation(ScooterLocationRequest request) {
    Scooter scooter = scooterRepository.findOneBySerial(request.getSerial());
    Location newLocation = request.getLocation().toEntity();

    scooter.changeLocation(newLocation);
    scooterRepository.save(scooter);

    return ModelMapper.mapScooterToScooterResponse(scooter);
  }

  private void validatePageNumberAndSize(int page, int size) {
    if (page < 0) {
      throw new BadRequestException("Page number cannot be less than zero.");
    }

    if (size > AppConstants.MAX_PAGE_SIZE) {
      throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
    }
  }

}
