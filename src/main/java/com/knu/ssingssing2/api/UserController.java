package com.knu.ssingssing2.api;

import com.knu.ssingssing2.exception.ResourceNotFoundException;
import com.knu.ssingssing2.model.User;
import com.knu.ssingssing2.model.reservation.Reservation;
import com.knu.ssingssing2.payload.response.UserIdentiyAvailability;
import com.knu.ssingssing2.payload.response.UserProfile;
import com.knu.ssingssing2.repository.UserRepository;
import com.knu.ssingssing2.security.CurrentUser;
import com.knu.ssingssing2.security.UserPrincipal;
import com.knu.ssingssing2.service.ReservationService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ReservationService reservationService;

  @GetMapping("/user/checkUsernameAvailability")
  public UserIdentiyAvailability checkUsernameAvailability(
      @RequestParam(value = "username") String username) {
    Boolean isAvailable = !userRepository.existsByUsername(username);
    return new UserIdentiyAvailability(isAvailable);
  }

  @GetMapping("/users/{username}")
  public UserProfile getUserProfile(@PathVariable(value = "username") String username) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

    return new UserProfile(user.getId(), user.getUsername(), user.getName(),
        user.getReservations());
  }


  @GetMapping("/users/{username}/reservations")
  @PreAuthorize("hasRole('USER')")
  public List<Reservation> getAllReservationList(@PathVariable(value = "username") String username,
      @CurrentUser UserPrincipal currentUser) {
    return reservationService.getReservationsByUser(username, currentUser);
  }
}
