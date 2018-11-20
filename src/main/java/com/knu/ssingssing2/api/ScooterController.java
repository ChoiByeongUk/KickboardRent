package com.knu.ssingssing2.api;

import com.knu.ssingssing2.payload.ScooterResponse;
import com.knu.ssingssing2.service.ScooterService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scooters")
public class ScooterController {

  @Autowired
  private ScooterService scooterService;

//  @GetMapping
//  public PagedResponse<ScooterResponse> getAllScootersWithPaging(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
//      @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
//    return scooterService.getAllScooters(page, size);
//  }

  @GetMapping
  public List<ScooterResponse> getAllScootersWithoutPaging() {
    return scooterService.getAllScooters();
  }

}
