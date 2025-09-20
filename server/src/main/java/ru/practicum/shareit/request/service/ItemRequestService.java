package ru.practicum.shareit.request.service;

import java.util.Collection;

import ru.practicum.shareit.request.dto.CreateItemRequestRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;

public interface ItemRequestService {

    Collection<ItemRequestResponseDto> getMyRequests(Long userId);

    Collection<ItemRequest> getAllRequests(Long userId);

    ItemRequestResponseDto getById(Long requestId);

    ItemRequest create(CreateItemRequestRequestDto request, Long currentUserId);

}
