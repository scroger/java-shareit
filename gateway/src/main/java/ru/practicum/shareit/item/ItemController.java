package ru.practicum.shareit.item;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.CreateItemRequestDto;
import ru.practicum.shareit.item.dto.UpdateItemRequestDto;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestHeader("X-Sharer-User-Id") Long currentUserId) {
        log.info("Getting all items for user {}", currentUserId);

        return itemClient.getAll(currentUserId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getById(@RequestHeader("X-Sharer-User-Id") Long currentUserId,
                                          @PathVariable("itemId") Long itemId) {
        log.info("Getting item {} for user {}", itemId, currentUserId);

        return itemClient.getById(itemId, currentUserId);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long currentUserId,
                                         @Valid @RequestBody CreateItemRequestDto createItemRequestDto) {
        log.info("Creating item for user {}. {}", currentUserId, createItemRequestDto);

        return itemClient.create(currentUserId, createItemRequestDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestHeader("X-Sharer-User-Id") Long currentUserId,
                                         @PathVariable("itemId") Long itemId,
                                         @Valid @RequestBody UpdateItemRequestDto item) {
        log.info("Updating item {} for user {}. {}", itemId, currentUserId, item);

        return itemClient.update(currentUserId, itemId, item);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam("text") String searchText) {
        log.info("Searching for {}", searchText);

        return itemClient.search(searchText);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") Long currentUserId,
                                             @Valid @RequestBody CommentRequestDto comment,
                                             @PathVariable("itemId") Long itemId) {
        log.info("Adding comment {} for user {} to item {}", comment, currentUserId, itemId);

        return itemClient.addComment(itemId, currentUserId, comment);
    }

}
