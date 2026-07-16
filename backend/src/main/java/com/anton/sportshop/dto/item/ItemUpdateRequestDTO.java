package com.anton.sportshop.dto.item;

import jakarta.validation.constraints.Positive;

import java.util.List;

public record ItemUpdateRequestDTO(
        Long id,
        String name,
        String description,
        String category,
        String image_url,
        @Positive Double price,
        List<String> types
) {
}
