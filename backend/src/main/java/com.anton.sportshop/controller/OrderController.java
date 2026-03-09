package com.anton.sportshop.controller;

import com.anton.sportshop.model.AppUser;
import com.anton.sportshop.model.CartItem;
import com.anton.sportshop.model.Order;
import com.anton.sportshop.model.OrderItem;
import com.anton.sportshop.service.impl.AppUserDetailsService;
import com.anton.sportshop.service.impl.CartService;
import com.anton.sportshop.service.impl.OrderService;
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

    @Autowired
    OrderService orderService;

    @Autowired
    AppUserDetailsService appUserDetailsService;

    @Autowired
    CartService cartService;

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
    ResponseEntity<?> makeOrder(@RequestBody Order order, @AuthenticationPrincipal UserDetails user){
        AppUser appUser = appUserDetailsService.loadAppUserByUsername(user.getUsername());

        List<CartItem> cartItems = cartService.getCartByUserId(appUser.getId()).getItems();
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
        orderService.makeOrder(order);
        return ResponseEntity.ok(order);
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

    @PutMapping("/{orderId}")
    ResponseEntity<?> putOrder(@PathVariable Long orderId, @RequestBody Order updatedOrder, @AuthenticationPrincipal UserDetails user) {
        Order order = orderService.getOrderById(orderId);
        AppUser appUser = appUserDetailsService.loadAppUserByUsername(user.getUsername());
        if (order.getUser().equals(appUser)) {
            try{
                orderService.updateOrder(order.getId(), updatedOrder);
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
