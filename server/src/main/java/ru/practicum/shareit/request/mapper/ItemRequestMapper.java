package ru.practicum.shareit.request.mapper;

import java.time.LocalDateTime;
import java.util.Collection;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.CreateItemRequestRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

public class ItemRequestMapper {

    public static ItemRequest map(CreateItemRequestRequestDto dto, User owner) {
        return ItemRequest.builder()
                .description(dto.description())
                .requestor(owner)
                .created(LocalDateTime.now())
                .build();
    }

    public static ItemRequestResponseDto map(ItemRequest itemRequest, Collection<Item> items) {
        return ItemRequestResponseDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requestorId(itemRequest.getRequestorId())
                .items(items)
                .created(itemRequest.getCreated())
                .build();
    }

}
