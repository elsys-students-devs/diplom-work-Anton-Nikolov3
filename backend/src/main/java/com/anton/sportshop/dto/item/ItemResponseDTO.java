package com.anton.sportshop.dto.item;

import java.util.List;

public record ItemResponseDTO(
        Long id,
        String name,
        String description,
        String category,
        String image_url,
        Double price,
        List<String> types
) {
}
