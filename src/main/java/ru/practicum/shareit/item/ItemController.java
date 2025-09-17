package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.CreateItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.UpdateItemRequestDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public Collection<Item> getAll(@RequestHeader("X-Sharer-User-Id") Long currentUserId) {
        return itemService.getAll(currentUserId);
    }

    @GetMapping("/{itemId}")
    public ItemResponseDto getById(@RequestHeader("X-Sharer-User-Id") Long currentUserId,
                                   @PathVariable("itemId") Long itemId) {
        return itemService.getById(itemId, currentUserId);
    }

    @PostMapping
    public Item create(@RequestHeader("X-Sharer-User-Id") Long currentUserId,
                       @Valid @RequestBody CreateItemRequestDto createItemRequestDto) {
        return itemService.create(currentUserId, createItemRequestDto);
    }

    @PatchMapping("/{itemId}")
    public Item update(@RequestHeader("X-Sharer-User-Id") Long currentUserId, @PathVariable("itemId") Long itemId,
                       @Valid @RequestBody UpdateItemRequestDto item) {
        return itemService.update(currentUserId, itemId, item);
    }

    @GetMapping("/search")
    public Collection<Item> search(@RequestParam("text") String searchText) {
        return itemService.search(searchText);
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponseDto addComment(@RequestHeader("X-Sharer-User-Id") Long currentUserId,
                                         @Valid @RequestBody CommentRequestDto comment,
                                         @PathVariable("itemId") Long itemId) {
        return itemService.addComment(itemId, currentUserId, comment);
    }

}
