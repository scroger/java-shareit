package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateBookingRequestDto(
        @Future
        @NotNull
        LocalDateTime start,

        @Future
        @NotNull
        LocalDateTime end,

        @NotNull
        Long itemId,

        Long bookerId
) {
}
