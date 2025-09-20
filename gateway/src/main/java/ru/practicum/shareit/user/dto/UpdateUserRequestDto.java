package ru.practicum.shareit.user.dto;

import lombok.Builder;

@Builder
public record UpdateUserRequestDto(
        String name,
        String email
) {
}
