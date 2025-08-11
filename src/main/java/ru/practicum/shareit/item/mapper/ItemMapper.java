package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.CreateItemRequestDto;
import ru.practicum.shareit.item.dto.UpdateItemRequestDto;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {

    public static Item map(Long ownerId, CreateItemRequestDto createItemDto) {
        return Item.builder()
                .id(null)
                .name(createItemDto.name())
                .description(createItemDto.description())
                .available(createItemDto.available())
                .ownerId(ownerId)
                .requestId(createItemDto.requestId())
                .build();
    }

    public static Item map(UpdateItemRequestDto updateItemDto, Item item) {
        return item.toBuilder()
                .name(null != updateItemDto.name() ? updateItemDto.name() : item.name())
                .description(null != updateItemDto.description() ? updateItemDto.description() : item.description())
                .available(null != updateItemDto.available() ? updateItemDto.available() : item.available())
                .requestId(null != updateItemDto.requestId() ? updateItemDto.requestId() : item.requestId())
                .build();
    }

}
