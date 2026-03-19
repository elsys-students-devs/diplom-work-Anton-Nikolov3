package com.anton.sportshop.service;

import com.anton.sportshop.exception.ResourceNotFoundException;
import com.anton.sportshop.model.Order;
import com.anton.sportshop.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getOrdersByUserId(Long userId){
        return orderRepository.findOrdersByUserId(userId);
    }

    public Order getOrderById(Long orderId){
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order was not found"));
    }

    public void makeOrder(Order order){
        orderRepository.save(order);
    }

    public void deleteOrderById(Long orderId){
        orderRepository.deleteById(orderId);
    }

    public void updateOrder(Long orderId, Order updatedOrder){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found!"));

        order.setAddress(updatedOrder.getAddress());
        order.setFirstName(updatedOrder.getFirstName());
        order.setLastName(updatedOrder.getLastName());
        order.setPhoneNumber(updatedOrder.getPhoneNumber());

        orderRepository.save(order);

    }


}
