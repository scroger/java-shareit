package ru.practicum.shareit.user.dto;

import lombok.Builder;

@Builder
public record CreateUserRequestDto(
        String name,
        String email
) {
}
