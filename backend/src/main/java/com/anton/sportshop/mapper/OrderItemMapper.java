package com.anton.sportshop.mapper;

import com.anton.sportshop.dto.order_item.OrderItemRequestDTO;
import com.anton.sportshop.dto.order_item.OrderItemResponseDTO;
import com.anton.sportshop.model.OrderItem;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {

    public OrderItem toEntity (OrderItemRequestDTO requestDTO){
        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(requestDTO.quantity());
        return orderItem;
    }

    public OrderItemResponseDTO toDto(OrderItem order){
        return new OrderItemResponseDTO(
                order.getItem().getId(),
                order.getItem().getName(),
                order.getQuantity(),
                order.getItem().getPrice()
        );
    }

}
