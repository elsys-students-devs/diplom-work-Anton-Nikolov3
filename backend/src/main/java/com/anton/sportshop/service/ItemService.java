package com.anton.sportshop.service;

import com.anton.sportshop.dto.item.ItemCreateRequestDTO;
import com.anton.sportshop.dto.item.ItemResponseDTO;
import com.anton.sportshop.dto.item.ItemUpdateRequestDTO;
import com.anton.sportshop.exception.ResourceNotFoundException;
import com.anton.sportshop.mapper.ItemMapper;
import com.anton.sportshop.model.AppUser;
import com.anton.sportshop.model.Item;
import com.anton.sportshop.repository.ItemRepository;
import com.anton.sportshop.repository.RatingRepository;
import com.anton.sportshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ItemService {


    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository,
                       ItemMapper itemMapper, UserRepository userRepository, RatingRepository ratingRepository) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
        this.userRepository = userRepository;
        this.ratingRepository = ratingRepository;
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

    @Transactional
    public void deleteItemById(long id) {

        var item = itemRepository.findById(id).orElseThrow();

        List<AppUser> users = userRepository.findAll();
        users.forEach(x->x.getFavoriteItems().remove(item));
        ratingRepository.deleteByItemId(id);

        userRepository.saveAll(users);
        itemRepository.deleteById(id);
    }
}
