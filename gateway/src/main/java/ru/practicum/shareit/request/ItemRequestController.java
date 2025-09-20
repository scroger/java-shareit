package ru.practicum.shareit.request;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.request.dto.CreateItemRequestRequestDto;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @GetMapping
    public ResponseEntity<Object> getMyRequests(@RequestHeader("X-Sharer-User-Id") Long currentUserId) {
        log.info("Getting item requests stored by user {}", currentUserId);

        return itemRequestClient.getMyRequests(currentUserId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@RequestHeader("X-Sharer-User-Id") Long currentUserId) {
        log.info("Getting all item requests for user {}", currentUserId);

        return itemRequestClient.getAllRequests(currentUserId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getById(@RequestHeader("X-Sharer-User-Id") Long currentUserId,
                                          @Positive @PathVariable Long requestId) {
        log.info("Getting item request {} for user {}", requestId, currentUserId);

        return itemRequestClient.getById(requestId, currentUserId);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long currentUserId,
                                         @Valid @RequestBody CreateItemRequestRequestDto request) {
        log.info("Creating item request {} for user {}", request, currentUserId);

        return itemRequestClient.create(request, currentUserId);
    }

}
