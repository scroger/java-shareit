package ru.practicum.shareit.item.dto;

import lombok.Builder;

@Builder
public record UpdateItemRequestDto(
        String name,
        String description,
        Boolean available,
        Long requestId
) {
}
