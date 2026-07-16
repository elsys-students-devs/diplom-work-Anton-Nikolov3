package com.anton.sportshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.anton.sportshop.model.AppUser;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
}
