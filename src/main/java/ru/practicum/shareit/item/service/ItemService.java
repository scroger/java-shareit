package ru.practicum.shareit.item.service;

import java.util.Collection;

import ru.practicum.shareit.item.dto.CreateItemRequestDto;
import ru.practicum.shareit.item.dto.UpdateItemRequestDto;
import ru.practicum.shareit.item.model.Item;

public interface ItemService {

    Collection<Item> getAll(Long userId);

    Item getById(Long itemId);

    Item create(Long userId, CreateItemRequestDto createItemRequestDto);

    Item update(Long userId, Long itemId, UpdateItemRequestDto updateItemRequestDto);

    Collection<Item> search(String searchText);

}
