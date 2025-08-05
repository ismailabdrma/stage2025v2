// src/main/java/com/example/stage2025/repository/UserRepository.java
package com.example.stage2025.repository;

import com.example.stage2025.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);
  Optional<User> findByEmail(String email);
  Optional<User> findByUsernameOrEmail(String username, String email);
  boolean existsByUsername(String username);
  boolean existsByEmail(String email);
  boolean existsByUsernameOrEmail(String username, String email);

}
