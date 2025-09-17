package ru.practicum.shareit.item.service;

import java.util.Collection;

import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.CreateItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.UpdateItemRequestDto;
import ru.practicum.shareit.item.model.Item;

public interface ItemService {

    Collection<Item> getAll(Long userId);

    Item getById(Long itemId);

    ItemResponseDto getById(Long itemId, Long currentUserId);

    Item create(Long userId, CreateItemRequestDto createItemRequestDto);

    Item update(Long userId, Long itemId, UpdateItemRequestDto updateItemRequestDto);

    Collection<Item> search(String searchText);

    CommentResponseDto addComment(Long itemId, Long userId, CommentRequestDto commentDto);
}
