package com.anton.sportshop.dto.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record ItemCreateRequestDTO(
        @NotBlank String name,
        @NotBlank String description,
        @NotBlank String category,
        @NotBlank String image_url,
        @NotNull @Positive Double price,
        List<String> types
) {
}
