package com.anton.sportshop.dto.rating;

import jakarta.validation.constraints.NotNull;

public record UserRatingGetRequestDTO(
        @NotNull Long itemId
) {
}
