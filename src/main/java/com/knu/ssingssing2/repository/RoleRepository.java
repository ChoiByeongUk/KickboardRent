package com.knu.ssingssing2.repository;

import com.knu.ssingssing2.model.Role;
import com.knu.ssingssing2.model.RoleName;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

  Optional<Role> findByName(RoleName roleName);

}
