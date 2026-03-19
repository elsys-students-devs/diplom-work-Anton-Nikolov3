package com.anton.sportshop.mapper;

import com.anton.sportshop.dto.item.ItemCreateRequestDTO;
import com.anton.sportshop.dto.item.ItemResponseDTO;
import com.anton.sportshop.dto.item.ItemUpdateRequestDTO;
import com.anton.sportshop.model.Item;
import org.springframework.stereotype.Component;

@Component
public class ItemMapper {
    public Item createToEntity(ItemCreateRequestDTO requestDTO){
        return mapAllFields(new Item(), requestDTO);
    }

    public Item updateEntity(Item item, ItemUpdateRequestDTO dto) {
        return updateFields(item, dto);
    }

    public ItemResponseDTO toDto(Item item){
        return new ItemResponseDTO(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getCategory(),
                item.getImage_url(),
                item.getPrice(),
                item.getTypes()
        );
    }


    private Item mapAllFields(Item item, ItemCreateRequestDTO dto) {
        item.setName(dto.name());
        item.setDescription(dto.description());
        item.setCategory(dto.category());
        item.setImage_url(dto.image_url());
        item.setPrice(dto.price());
        item.setTypes(dto.types());

        return item;
    }
    private Item updateFields(Item item, ItemUpdateRequestDTO dto) {

        if (dto.name() != null) item.setName(dto.name());
        if (dto.description() != null) item.setDescription(dto.description());
        if (dto.category() != null) item.setCategory(dto.category());
        if (dto.image_url() != null) item.setImage_url(dto.image_url());
        if (dto.price() != null) item.setPrice(dto.price());
        if (dto.types() != null) item.setTypes(dto.types());

        return item;
    }
}
