package ru.practicum.shareit.item.service.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.error.exception.ForbiddenException;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CreateItemRequestDto;
import ru.practicum.shareit.item.dto.UpdateItemRequestDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    public final ItemRepository itemRepository;
    public final UserRepository userRepository;

    @Override
    public Collection<Item> getAll(Long userId) {
        return itemRepository.findByUserId(userId);
    }

    @Override
    public Item getById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Item with id=%d not found", itemId)));
    }

    @Override
    public Item create(Long userId, CreateItemRequestDto createItemRequestDto) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id=%d not found", userId)));

        return itemRepository.create(ItemMapper.map(userId, createItemRequestDto));
    }

    @Override
    public Item update(Long userId, Long itemId, UpdateItemRequestDto updateItemRequestDto) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id=%d not found", userId)));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Item with id=%d not found", itemId)));

        if (!Objects.equals(userId, item.ownerId())) {
            throw new ForbiddenException("Forbidden");
        }

        return itemRepository.update(ItemMapper.map(updateItemRequestDto, item));
    }

    @Override
    public Collection<Item> search(String searchText) {
        if (searchText.isBlank()) {
            return Collections.emptyList();
        }

        return itemRepository.search(searchText);
    }

}
