package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateItemRequestDto(
        @NotBlank
        String name,

        @NotBlank
        String description,

        @NotNull
        Boolean available,

        Long requestId
) {
}
