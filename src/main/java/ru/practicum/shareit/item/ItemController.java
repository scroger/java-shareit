package ru.practicum.shareit.item;

import java.util.Collection;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.item.dto.CreateItemRequestDto;
import ru.practicum.shareit.item.dto.UpdateItemRequestDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

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
    public Item getById(@PathVariable("itemId") Long itemId) {
        return itemService.getById(itemId);
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

}
