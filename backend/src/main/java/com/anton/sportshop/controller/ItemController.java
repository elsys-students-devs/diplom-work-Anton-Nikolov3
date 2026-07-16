package com.anton.sportshop.controller;

import com.anton.sportshop.dto.item.ItemCreateRequestDTO;
import com.anton.sportshop.dto.item.ItemUpdateRequestDTO;
import com.anton.sportshop.model.AppUser;
import com.anton.sportshop.model.Item;
import com.anton.sportshop.service.AppUserDetailsService;
import com.anton.sportshop.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/item")

public class ItemController {

    private final ItemService itemService;
    private final AppUserDetailsService appUserDetailsService;


    public ItemController(ItemService itemService, AppUserDetailsService appUserDetailsService){
        this.itemService = itemService;
        this.appUserDetailsService = appUserDetailsService;
    }


    @GetMapping
    public List<Item> getAllItems(){
        return itemService.getAllItems();
    }

    @GetMapping("/{id}")
    public Item getItem(@PathVariable long id){
        return itemService.getItemById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> addItem(@Valid @RequestBody ItemCreateRequestDTO createRequestDTO){
        return ResponseEntity.ok(itemService.addItem(createRequestDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItemById(@PathVariable long id){
        itemService.deleteItemById(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateItem(@PathVariable long id, @Valid @RequestBody ItemUpdateRequestDTO requestDTO){
        return ResponseEntity.ok(itemService.updateItemById(id, requestDTO));
    }

    @GetMapping("/favorites")
    public ResponseEntity<?> getUserFavourites(@AuthenticationPrincipal UserDetails user){
        AppUser appUser = appUserDetailsService.loadAppUserByUsername(user.getUsername());

        return ResponseEntity.ok(appUser.getFavoriteItems());
    }

    @PostMapping("/favorites/{itemId}")
    public ResponseEntity<?> addUserFavorite(@AuthenticationPrincipal UserDetails user, @PathVariable Long itemId){
        Item item = itemService.getItemById(itemId);
        appUserDetailsService.addFavoriteProduct(user.getUsername(),item);
        return ResponseEntity.ok("Added favorite");
    }

    @DeleteMapping("/favorites/{itemId}")
    public ResponseEntity<?> deleteUserFavorite(@AuthenticationPrincipal UserDetails user, @PathVariable Long itemId){
        Item item = itemService.getItemById(itemId);
        appUserDetailsService.deleteFavoriteProduct(user.getUsername(),item);
        return ResponseEntity.ok("Deleted favorite");
    }

}
