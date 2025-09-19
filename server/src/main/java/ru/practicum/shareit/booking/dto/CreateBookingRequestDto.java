package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record CreateBookingRequestDto(
        LocalDateTime start,

        LocalDateTime end,

        Long itemId
) {
}
