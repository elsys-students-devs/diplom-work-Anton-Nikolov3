package com.anton.sportshop.service.impl;

import com.anton.sportshop.model.AppUser;
import com.anton.sportshop.model.Cart;
import com.anton.sportshop.model.CartItem;
import com.anton.sportshop.model.Item;
import com.anton.sportshop.repository.CartItemRepository;
import com.anton.sportshop.repository.CartRepository;
import com.anton.sportshop.repository.ItemRepository;
import com.anton.sportshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    public Cart getCartByUserId(Long id){
        return cartRepository.findByUserId(id);
    }

    public Cart addToCart(Long userId, Long itemId, int quantity){
        AppUser user = userRepository.findById(userId).orElse(null);
        Item item = itemRepository.findById(itemId).orElse(null);

        Cart cart = user.getCart();
        if(cart == null){
            cart = new Cart(user);
            user.setCart(cart);
        }

        if(item != null && cart.getItems().stream().anyMatch( i -> i.getItem().getId().equals(itemId))){
            cart.getItems().stream()
                    .filter(i -> i.getItem().getId().equals(itemId))
                    .findFirst()
                    .ifPresent(i -> i.setQuantity(i.getQuantity() + quantity));
        }
        else{
            CartItem cartItem = new CartItem(cart, item, quantity);
            cart.addItem(cartItem);
        }



        return cartRepository.save(cart);



    }

    public Cart removeItem(Long userId, Long itemId){
        Cart cart = cartRepository.findByUserId(userId);

        var cartItem = cart.getItems().stream().filter(i -> i.getItem().getId().equals(itemId)).findFirst();
        cart.removeItem(cartItem.get());

        return cartRepository.save(cart);

    }

    public Cart editQuantity(Long userId, Long itemId, int quantity){
        Cart cart = getCartByUserId(userId);

        cart.getItems().stream().filter(i -> i.getItem().getId().equals(itemId)).findFirst().ifPresent(i -> i.setQuantity(quantity));


        return cartRepository.save(cart);
    }

    public void clearItems(Long userId){
        Cart cart = getCartByUserId(userId);
        cartRepository.deleteById(cart.getId());
    }

}
