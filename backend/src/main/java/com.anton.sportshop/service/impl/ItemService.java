package com.anton.sportshop.service.impl;

import com.anton.sportshop.model.Item;
import com.anton.sportshop.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService implements com.anton.sportshop.service.ItemService {

    @Autowired
    ItemRepository itemRepository;

    @Override
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    @Override
    public Item getItemById(long id) {
        return itemRepository.findById(id).orElse(null);
    }

    @Override
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

    @Override
    public void addItem(Item item) {
        itemRepository.save(item);
    }

    @Override
    public void deleteItemById(long id) {
        itemRepository.deleteById(id);
    }
}
