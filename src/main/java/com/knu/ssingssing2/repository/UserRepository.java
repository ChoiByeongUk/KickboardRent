package com.knu.ssingssing2.repository;

import com.knu.ssingssing2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  User findOneById(Long id);

}
