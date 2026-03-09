package com.anton.sportshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.anton.sportshop.model.AppUser;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
}
