package com.anton.sportshop.repository;

import com.anton.sportshop.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    Optional<Rating> findByUserIdAndItemId(Long userID, Long itemId);

    List<Rating> findByItemId(Long itemId);
    void deleteByItemId(Long itemId);
}
