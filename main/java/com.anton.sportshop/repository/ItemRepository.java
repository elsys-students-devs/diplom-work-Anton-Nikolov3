package com.anton.sportshop.repository;

import com.anton.sportshop.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item,Long> {
}
