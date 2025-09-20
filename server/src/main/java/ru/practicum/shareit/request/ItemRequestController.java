package ru.practicum.shareit.request;

import java.util.Collection;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.request.dto.CreateItemRequestRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @GetMapping
    public Collection<ItemRequestResponseDto> getMyRequests(@RequestHeader("X-Sharer-User-Id") Long currentUserId) {
        return itemRequestService.getMyRequests(currentUserId);
    }

    @GetMapping("/all")
    public Collection<ItemRequest> getAllRequests(@RequestHeader("X-Sharer-User-Id") Long currentUserId) {
        return itemRequestService.getAllRequests(currentUserId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestResponseDto getById(@RequestHeader("X-Sharer-User-Id") Long currentUserId,
                                          @PathVariable Long requestId) {
        return itemRequestService.getById(requestId);
    }

    @PostMapping
    public ItemRequest create(@RequestHeader("X-Sharer-User-Id") Long currentUserId,
                              @RequestBody CreateItemRequestRequestDto request) {
        return itemRequestService.create(request, currentUserId);
    }

}
