package ru.practicum.shareit.item.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentRequestDto(
        Long id,

        @NotNull
        @NotBlank
        String text,

        LocalDate created
) {
}
