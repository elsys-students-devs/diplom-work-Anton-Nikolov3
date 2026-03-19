package com.anton.sportshop.dto.order_item;

import jakarta.validation.constraints.Positive;

public record OrderItemRequestDTO(
        @Positive Long itemId,
        @Positive int quantity
) {
}
