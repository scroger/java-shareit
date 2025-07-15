package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateUserRequestDto(
        @NotNull
        String name,

        @NotNull
        @Email
        String email
) {
}
