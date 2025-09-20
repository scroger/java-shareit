package ru.practicum.shareit.request.service.impl;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.CreateItemRequestRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public Collection<ItemRequestResponseDto> getMyRequests(Long userId) {
        Collection<ItemRequest> itemRequests = itemRequestRepository.findAllByRequestorId(userId);

        Collection<Item> itemsList = itemRepository.findAllByRequestIdIn(itemRequests.stream()
                .map(ItemRequest::getId)
                .collect(Collectors.toSet()));

        return itemRequests.stream()
                .map(itemRequest -> ItemRequestMapper.map(itemRequest, itemsList.stream()
                        .filter(item -> item.getRequest().getId().equals(itemRequest.getId()))
                        .toList()))
                .toList();
    }

    @Override
    public Collection<ItemRequest> getAllRequests(Long userId) {
        return itemRequestRepository.findAllByRequestorIdNot(userId);
    }

    @Override
    public ItemRequestResponseDto getById(Long requestId) {
        return ItemRequestMapper.map(
                itemRequestRepository.findById(requestId).orElseThrow(() -> new NotFoundException(
                        String.format("Item request with id=%d not found", requestId))),
                itemRepository.findAllByRequestId(requestId)
        );
    }

    @Override
    public ItemRequest create(CreateItemRequestRequestDto request, Long currentUserId) {
        return itemRequestRepository.save(ItemRequestMapper.map(request, userRepository.findById(currentUserId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id=%d not found", currentUserId)))));
    }

}
