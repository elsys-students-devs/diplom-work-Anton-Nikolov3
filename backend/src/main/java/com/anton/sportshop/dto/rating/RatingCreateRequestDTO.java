package com.anton.sportshop.dto.rating;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RatingCreateRequestDTO(
        @NotNull Long itemId,
        @NotNull @Positive Integer stars
) {
}
