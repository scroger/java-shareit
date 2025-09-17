package ru.practicum.shareit.comment.dto;

import java.time.LocalDate;

public record CommentRequestDto(
        Long id,

        String text,

        LocalDate created
) {
}
