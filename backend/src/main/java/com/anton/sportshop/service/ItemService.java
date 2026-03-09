package com.anton.sportshop.service;

import com.anton.sportshop.model.Item;
import com.anton.sportshop.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {


    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Item getItemById(long id) {
        return itemRepository.findById(id).orElse(null);
    }

    public void updateItemById(long id, Item item) {
        Item updateItem = itemRepository.findById(id).orElse(null);
        if(updateItem == null){
            return;
        }

        updateItem.setName(item.getName());
        updateItem.setDescription(item.getDescription());
        updateItem.setCategory(item.getCategory());
        updateItem.setPrice(item.getPrice());
        updateItem.setImage_url(item.getImage_url());
        updateItem.setTypes(item.getTypes());

        itemRepository.save(updateItem);

    }

    public void addItem(Item item) {
        itemRepository.save(item);
    }

    public void deleteItemById(long id) {
        itemRepository.deleteById(id);
    }
}
