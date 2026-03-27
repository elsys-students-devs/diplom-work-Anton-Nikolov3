package com.anton.sportshop.service;

import com.anton.sportshop.exception.ResourceNotFoundException;
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

@Service
public class CartService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;

    @Autowired
    public CartService(UserRepository userRepository,
                       ItemRepository itemRepository,
                       CartRepository cartRepository) {

        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.cartRepository = cartRepository;
    }

    public Cart getCartByUserId(Long id){
        return cartRepository.findByUserId(id);
    }

    public Cart addToCart(Long userId, Long itemId, int quantity){
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));

        Cart cart = user.getCart();

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
        cart.getItems().clear();
        cartRepository.save(cart);
    }

}
