package com.anton.sportshop.controller;

import com.anton.sportshop.dto.order.OrderCreateRequestDTO;
import com.anton.sportshop.dto.order.OrderUpdateRequestDTO;
import com.anton.sportshop.mapper.OrderMapper;
import com.anton.sportshop.model.*;
import com.anton.sportshop.service.AppUserDetailsService;
import com.anton.sportshop.service.CartService;
import com.anton.sportshop.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/order")
@RestController
public class OrderController {

    private final OrderService orderService;
    private final AppUserDetailsService appUserDetailsService;
    private final CartService cartService;
    private final OrderMapper orderMapper;

    @Autowired
    public OrderController(OrderService orderService,
                           AppUserDetailsService appUserDetailsService,
                           CartService cartService,
                           OrderMapper orderMapper) {
        this.orderService = orderService;
        this.appUserDetailsService = appUserDetailsService;
        this.cartService = cartService;
        this.orderMapper = orderMapper;
    }

    @GetMapping
    List<Order> getAllOrders(@AuthenticationPrincipal UserDetails user) {
        Long userId = appUserDetailsService.loadAppUserByUsername(user.getUsername()).getId();
        return orderService.getOrdersByUserId(userId);
    }

    @GetMapping("/{orderId}")
    ResponseEntity<?> getOrderById(@PathVariable Long orderId, @AuthenticationPrincipal UserDetails user){
        Order order = orderService.getOrderById(orderId);
        AppUser appUser = appUserDetailsService.loadAppUserByUsername(user.getUsername());
        if(order.getUser().equals(appUser)){

            return ResponseEntity.ok(order);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }

    @PostMapping
    ResponseEntity<?> makeOrder(@Valid @RequestBody OrderCreateRequestDTO createRequestDTO, @AuthenticationPrincipal UserDetails user){
        AppUser appUser = appUserDetailsService.loadAppUserByUsername(user.getUsername());

        List<CartItem> cartItems = cartService.getCartByUserId(appUser.getId()).getItems();
        Order order = orderMapper.createToEntity(createRequestDTO);

        if(cartItems.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You cart is empty");
        }

        List<OrderItem> items = cartItems.stream().map(ci -> {
            OrderItem oi = new OrderItem();
            oi.setItem(ci.getItem());
            oi.setQuantity(ci.getQuantity());
            oi.setPrice(ci.getItem().getPrice() * ci.getQuantity());
            oi.setOrder(order);
            return oi;
        }).toList();

        order.setUser(appUser);
        order.setItems(items);
        order.setPrice(items.stream().mapToDouble(OrderItem::getPrice).sum());

        cartService.clearItems(appUser.getId());
        orderService.makeOrder(order, appUser);
        return ResponseEntity.ok(orderMapper.toDto(order));
    }

    @DeleteMapping("/{orderId}")
    ResponseEntity<?> deleteOrder(@PathVariable Long orderId, @AuthenticationPrincipal UserDetails user) {
        Order order = orderService.getOrderById(orderId);
        AppUser appUser = appUserDetailsService.loadAppUserByUsername(user.getUsername());
        if (order.getUser().equals(appUser)) {
            orderService.deleteOrderById(orderId);
            return ResponseEntity.noContent().build();
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PatchMapping("/{orderId}")
    ResponseEntity<?> putOrder(@PathVariable Long orderId, @Valid @RequestBody OrderUpdateRequestDTO orderUpdateRequestDTO, @AuthenticationPrincipal UserDetails user) {
        Order order = orderService.getOrderById(orderId);
        AppUser appUser = appUserDetailsService.loadAppUserByUsername(user.getUsername());
        if (order.getUser().equals(appUser)) {
            try{
                orderService.updateOrder(order.getId(), orderMapper.updateEntity(order, orderUpdateRequestDTO));
            } catch (RuntimeException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.CREATED).body("Order updated");
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
