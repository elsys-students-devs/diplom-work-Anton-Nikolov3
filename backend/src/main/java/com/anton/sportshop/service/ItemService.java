package com.anton.sportshop.service;

import com.anton.sportshop.dto.item.ItemCreateRequestDTO;
import com.anton.sportshop.dto.item.ItemResponseDTO;
import com.anton.sportshop.dto.item.ItemUpdateRequestDTO;
import com.anton.sportshop.exception.ResourceNotFoundException;
import com.anton.sportshop.mapper.ItemMapper;
import com.anton.sportshop.model.Item;
import com.anton.sportshop.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {


    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Autowired
    public ItemService(ItemRepository itemRepository,
                       ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Item getItemById(long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));
    }

    public ItemResponseDTO updateItemById(long id, ItemUpdateRequestDTO requestDTO) {

        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found!"));
        Item itemToUpdate = itemMapper.updateEntity(item, requestDTO);

        itemRepository.save(itemToUpdate);
        return itemMapper.toDto(itemToUpdate);
    }

    public ItemResponseDTO addItem(ItemCreateRequestDTO createRequestDTO) {
        Item item = itemMapper.createToEntity(createRequestDTO);
        itemRepository.save(item);

        return itemMapper.toDto(item);
    }

    public void deleteItemById(long id) {
        itemRepository.deleteById(id);
    }
}
