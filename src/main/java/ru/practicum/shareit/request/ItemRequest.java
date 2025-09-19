package ru.practicum.shareit.request;

import java.time.LocalDateTime;

import lombok.Builder;
import ru.practicum.shareit.user.model.User;

@Builder
public record ItemRequest(
        Long id,
        String description,
        User requestor,
        LocalDateTime created
) {
}
