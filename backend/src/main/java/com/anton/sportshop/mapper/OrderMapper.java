package com.anton.sportshop.mapper;


import com.anton.sportshop.dto.order.OrderCreateRequestDTO;
import com.anton.sportshop.dto.order.OrderResponseDTO;
import com.anton.sportshop.dto.order.OrderUpdateRequestDTO;
import com.anton.sportshop.dto.order_item.OrderItemResponseDTO;
import com.anton.sportshop.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {


    private final OrderItemMapper orderItemMapper;

    @Autowired
    public OrderMapper(OrderItemMapper orderItemMapper){
        this.orderItemMapper = orderItemMapper;
    }

    public Order createToEntity(OrderCreateRequestDTO requestDTO){
        return mapAllFields(new Order(), requestDTO);
    }

    public Order updateEntity(Order order, OrderUpdateRequestDTO dto) {
        return updateFields(order, dto);
    }

    public OrderResponseDTO toDto(Order order){
        List<OrderItemResponseDTO> items = order.getItems() == null
                ? List.of()
                : order.getItems().stream()
                .map(orderItemMapper::toDto)
                .toList();

        return new OrderResponseDTO(
                order.getFirstName(),
                order.getLastName(),
                order.getPhoneNumber(),
                order.getAddress(),
                order.getPrice(),
                items
        );
    }

    private Order mapAllFields(Order order, OrderCreateRequestDTO dto) {
        order.setFirstName(dto.firstName());
        order.setLastName(dto.lastName());
        order.setPhoneNumber(dto.phoneNumber());
        order.setAddress(dto.address());


        return order;
    }
    private Order updateFields(Order order, OrderUpdateRequestDTO dto) {

        if (dto.firstName() != null) order.setFirstName(dto.firstName());
        if (dto.lastName() != null) order.setLastName(dto.lastName());
        if (dto.phoneNumber() != null) order.setPhoneNumber(dto.phoneNumber());
        if (dto.address() != null) order.setAddress(dto.address());

        return order;
    }
}
