package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Builder
public record BookingResponseDto(
        Long id,

        LocalDateTime start,

        LocalDateTime end,

//        Long itemId,
        Item item,

//        Long bookerId,
        User booker,

        Booking.Status status
) {
}
