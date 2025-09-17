package ru.practicum.shareit.comment.dto;

import java.time.LocalDate;

import lombok.Builder;

@Builder
public record CommentResponseDto(
        Long id,

        String text,

        String authorName,

        LocalDate created
) {
}
