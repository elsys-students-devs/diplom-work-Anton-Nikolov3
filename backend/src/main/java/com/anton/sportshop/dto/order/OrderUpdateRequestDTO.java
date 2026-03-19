package com.anton.sportshop.dto.order;

import com.anton.sportshop.dto.order_item.OrderItemRequestDTO;

import java.util.List;

public record OrderUpdateRequestDTO(
        String firstName,
        String lastName,
        String phoneNumber,
        String address,
        List<OrderItemRequestDTO> items
) {
}
