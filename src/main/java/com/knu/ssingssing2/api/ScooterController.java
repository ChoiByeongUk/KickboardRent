package com.knu.ssingssing2.api;

import com.knu.ssingssing2.model.scooter.Scooter;
import com.knu.ssingssing2.payload.request.ScooterLocationRequest;
import com.knu.ssingssing2.payload.request.ScooterReturnRequest;
import com.knu.ssingssing2.payload.response.ApiResponse;
import com.knu.ssingssing2.security.CurrentUser;
import com.knu.ssingssing2.security.UserPrincipal;
import com.knu.ssingssing2.service.ScooterService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scooters")
public class ScooterController {

  private final ScooterService scooterService;

  @Autowired
  public ScooterController(ScooterService scooterService) {
    this.scooterService = scooterService;
  }

  @GetMapping
  public List<Scooter> getAllScootersWithoutPaging() {
    return scooterService.getAllScooters();
  }

  @PutMapping("/location")
  public ResponseEntity<ApiResponse> updateCurrentScooterLocation(@RequestBody
      ScooterLocationRequest request) {

    scooterService.updateScooterLocation(request);
    return new ResponseEntity<>(new ApiResponse(true, "success change scooter current location"),
        HttpStatus.OK);
  }

  @PutMapping("/return")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<ApiResponse> returnScooter(
      @CurrentUser UserPrincipal currentUser,
      @RequestBody ScooterReturnRequest request) {

    scooterService.returnScooter(currentUser, request);

    return new ResponseEntity<>(new ApiResponse(true, "success return scooter"),
        HttpStatus.OK);
  }

}
