package ru.practicum.shareit.comment.dto;

import java.time.LocalDate;

import lombok.Builder;

@Builder
public record CommentRequestDto(
        Long id,

        String text,

        LocalDate created
) {
}
