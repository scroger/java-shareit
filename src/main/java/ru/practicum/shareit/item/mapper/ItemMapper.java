package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.CreateItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.UpdateItemRequestDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

public class ItemMapper {

    public static Item map(User owner, CreateItemRequestDto createItemDto) {
        return Item.builder()
                .name(createItemDto.name())
                .description(createItemDto.description())
                .available(createItemDto.available())
                .owner(owner)
                .requestId(createItemDto.requestId())
                .build();
    }

    public static Item map(UpdateItemRequestDto updateItemDto, Item item) {
        Optional.ofNullable(updateItemDto.name())
                .ifPresent(item::setName);

        Optional.ofNullable(updateItemDto.description())
                .ifPresent(item::setDescription);

        Optional.ofNullable(updateItemDto.available())
                .ifPresent(item::setAvailable);

        Optional.ofNullable(updateItemDto.requestId())
                .ifPresent(item::setRequestId);

        return item;
    }

    public static ItemResponseDto map(Item item) {
        return ItemResponseDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .ownerId(item.getOwner().getId())
                .build();
    }

}
