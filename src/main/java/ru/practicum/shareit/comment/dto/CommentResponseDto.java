package ru.practicum.shareit.comment.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record CommentResponseDto(
        Long id,

        String text,

        String authorName,

        LocalDate created
) {
}
