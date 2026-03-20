package com.anton.sportshop.dto.order;

import com.anton.sportshop.dto.order_item.OrderItemResponseDTO;

import java.util.List;

public record OrderResponseDTO(
        String firstName,
        String lastName,
        String phoneNumber,
        String address,
        Double price,
        List<OrderItemResponseDTO> items
) {
}
