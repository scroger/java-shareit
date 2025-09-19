package ru.practicum.shareit.item.mapper;

import java.util.Optional;

import ru.practicum.shareit.item.dto.CreateItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.UpdateItemRequestDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

public class ItemMapper {

    public static Item map(User owner, CreateItemRequestDto dto, ItemRequest itemRequest) {
        return Item.builder()
                .name(dto.name())
                .description(dto.description())
                .available(dto.available())
                .owner(owner)
                .request(itemRequest)
                .build();
    }

    public static Item map(UpdateItemRequestDto dto, Item item, ItemRequest itemRequest) {
        Optional.ofNullable(dto.name())
                .ifPresent(item::setName);

        Optional.ofNullable(dto.description())
                .ifPresent(item::setDescription);

        Optional.ofNullable(dto.available())
                .ifPresent(item::setAvailable);

        Optional.ofNullable(itemRequest)
                .ifPresent(item::setRequest);

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
