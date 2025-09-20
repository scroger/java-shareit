package ru.practicum.shareit.request.dto;

import java.time.LocalDateTime;
import java.util.Collection;

import lombok.Builder;
import ru.practicum.shareit.item.model.Item;

@Builder(toBuilder = true)
public record ItemRequestResponseDto(
        Long id,
        String description,
        Long requestorId,
        Collection<Item> items,
        LocalDateTime created
) {
}
