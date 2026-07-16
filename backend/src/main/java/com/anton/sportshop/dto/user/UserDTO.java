package com.anton.sportshop.dto.user;

import jakarta.validation.constraints.Email;

public record UserDTO(@Email String email) {
}
