package com.anton.sportshop.dto.order_item;

public record OrderItemResponseDTO(
        Long itemId,
        String itemName,
        int quantity,
        Double price
) {
}
