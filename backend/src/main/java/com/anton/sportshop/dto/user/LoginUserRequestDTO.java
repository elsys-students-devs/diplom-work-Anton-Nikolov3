package com.anton.sportshop.dto.user;

import jakarta.validation.constraints.NotBlank;

public record LoginUserRequestDTO(@NotBlank String username, @NotBlank String password) {
}
