package com.anton.sportshop.service.impl;

import com.anton.sportshop.model.CartItem;
import com.anton.sportshop.model.Order;
import com.anton.sportshop.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;

    public List<Order> getOrdersByUserId(Long userId){
        return orderRepository.findOrdersByUserId(userId);
    }

    public Order getOrderById(Long orderId){
        return orderRepository.findById(orderId).orElse(null);
    }

    public void makeOrder(Order order){
        orderRepository.save(order);
    }

    public void deleteOrderById(Long orderId){
        orderRepository.deleteById(orderId);
    }

    public void updateOrder(Long orderId, Order updatedOrder){
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found!"));

        order.setItems(updatedOrder.getItems());
        order.setPrice(updatedOrder.getPrice());
        order.setAddress(updatedOrder.getAddress());
        order.setFirstName(updatedOrder.getFirstName());
        order.setLastName(updatedOrder.getLastName());
        order.setPhoneNumber(updatedOrder.getPhoneNumber());

        orderRepository.save(order);

    }


}
