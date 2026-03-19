package com.anton.sportshop.dto.order;

import com.anton.sportshop.dto.order_item.OrderItemRequestDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record OrderCreateRequestDTO(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String phoneNumber,
        @NotBlank String address,
        @NotEmpty List<OrderItemRequestDTO> items
) {
}
