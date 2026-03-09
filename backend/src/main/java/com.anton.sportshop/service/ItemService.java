package com.anton.sportshop.service;

import com.anton.sportshop.model.Item;
import java.util.List;

public interface ItemService {

    List<Item> getAllItems();

    Item getItemById(long id);

    void updateItemById(long id, Item item);

    void addItem(Item item);

    void deleteItemById(long id);
}
