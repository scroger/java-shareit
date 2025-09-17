package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;

public record CreateBookingRequestDto(
        LocalDateTime start,

        LocalDateTime end,

        Long itemId
) {
}
