package com.knu.ssingssing2.api;

import com.knu.ssingssing2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  // TODO authorization 기능 완성 후 개발
  // user token 받아야 함 (@CurrentUser UserPrincipal currentUser)
  @GetMapping("/reservations")
  public ResponseEntity<?> getAllUserReservations() {
    return null;
  }

}
