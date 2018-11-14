package com.knu.ssingssing2.api;

import com.knu.ssingssing2.payload.ApiResponse;
import com.knu.ssingssing2.payload.ScooterLocationRequest;
import com.knu.ssingssing2.payload.ScooterResponse;
import com.knu.ssingssing2.service.ScooterService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scooters")
public class ScooterController {

  private Logger logger = LoggerFactory.getLogger(this.getClass());

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

  @PutMapping("/location")
  public ResponseEntity<ApiResponse> updateCurrentScooterLocation(@RequestBody
      ScooterLocationRequest request) {

    scooterService.updateScooterLocation(request);
    return new ResponseEntity(new ApiResponse(true, "success change scooter current location"), HttpStatus.OK);
  }
}
