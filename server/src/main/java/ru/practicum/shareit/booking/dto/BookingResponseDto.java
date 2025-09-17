package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Builder
public record BookingResponseDto(
        Long id,

        LocalDateTime start,

        LocalDateTime end,

        Item item,

        User booker,

        BookingStatus status
) {
}
