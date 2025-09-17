package ru.practicum.shareit.item.dto;

import lombok.Builder;
import ru.practicum.shareit.comment.dto.CommentResponseDto;

import java.time.LocalDateTime;
import java.util.Collection;

@Builder(toBuilder = true)
public record ItemResponseDto(
        Long id,
        String name,
        String description,
        Boolean available,
        Long ownerId,
        LocalDateTime lastBooking,
        LocalDateTime nextBooking,
        Collection<CommentResponseDto> comments
) {
}
