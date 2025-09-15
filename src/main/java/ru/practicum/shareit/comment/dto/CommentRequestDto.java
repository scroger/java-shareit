package ru.practicum.shareit.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CommentRequestDto(
        Long id,

        @NotNull
        @NotBlank
        String text,

        LocalDate created
) {
}
