package com.synrgy.kaboor.backend.repository;

import com.synrgy.kaboor.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByFullName(String username);

    Optional<User> findByEmail(String email);

}
