package com.anton.sportshop.dto.order;

public record OrderResponseDTO(
        String firstName,
        String lastName,
        String phoneNumber,
        String address,
        Double price
) {
}
