package com.anton.sportshop.controller;

import com.anton.sportshop.model.AppUser;
import com.anton.sportshop.model.Cart;
import com.anton.sportshop.model.CartItem;
import com.anton.sportshop.service.AppUserDetailsService;
import com.anton.sportshop.service.CartService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final AppUserDetailsService appUserDetailsService;

    public CartController(CartService cartService,
                          AppUserDetailsService appUserDetailsService) {
        this.cartService = cartService;
        this.appUserDetailsService = appUserDetailsService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{userId}")
    List<CartItem> getCartItemsByUserId(@PathVariable Long userId){
        return cartService.getCartByUserId(userId).getItems();
    }

    @GetMapping
    List<CartItem> getCartItems(@AuthenticationPrincipal UserDetails user){
        AppUser appUser = appUserDetailsService
                .loadAppUserByUsername(user.getUsername());

        return cartService.getCartByUserId(appUser.getId()).getItems();
    }

    /*
        {
            "itemId": ?,
            "quantity": ?
        }

     */

    @PostMapping
    List<CartItem> addItemToCart(@RequestBody JsonNode json, @AuthenticationPrincipal UserDetails user){

        Long userId = appUserDetailsService.loadAppUserByUsername(user.getUsername()).getId();

        Long itemId = json.get("itemId").asLong();
        Cart cart = cartService.getCartByUserId(userId);

        int quantity = json.get("quantity").asInt();


        cartService.addToCart(userId, itemId, quantity);


        return cart.getItems();
    }


    @PutMapping("/edit")
    List<CartItem> editItemQuantity(@RequestBody JsonNode json, @AuthenticationPrincipal UserDetails user){
        Long userId = appUserDetailsService.loadAppUserByUsername(user.getUsername()).getId();
        Long itemId = json.get("itemId").asLong();
        int quantity = json.get("quantity").asInt();
        return cartService.editQuantity(userId, itemId, quantity).getItems();
    }


    @DeleteMapping("/{itemId}")
    List<CartItem> deleteItem(@PathVariable Long itemId, @AuthenticationPrincipal UserDetails user){
        Long userId = appUserDetailsService.loadAppUserByUsername(user.getUsername()).getId();
        return cartService.removeItem(userId, itemId).getItems();
    }


}
