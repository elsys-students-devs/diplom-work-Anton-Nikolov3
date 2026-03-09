package com.anton.sportshop.controller;

import com.anton.sportshop.model.Item;
import com.anton.sportshop.service.ItemService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/item")

public class ItemController {


    private final ItemService itemService;

    public ItemController(ItemService itemService){
        this.itemService = itemService;
    }


    @GetMapping
    List<Item> getAllItems(){
        return itemService.getAllItems();
    }

    @GetMapping("/{id}")
    Item getItem(@PathVariable long id){
        return itemService.getItemById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    Item addItem(@RequestBody Item item){
        itemService.addItem(item);
        return item;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteItemById(@PathVariable long id){
        itemService.deleteItemById(id);

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    Item updateItem(@PathVariable long id, @RequestBody Item item){
        itemService.updateItemById(id, item);
        return item;
    }

}
