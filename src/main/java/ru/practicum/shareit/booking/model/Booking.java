package ru.practicum.shareit.booking.model;

import java.time.LocalDateTime;

import lombok.Builder;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Builder
public record Booking(
        Long id,
        LocalDateTime start,
        LocalDateTime end,
        Item item,
        User booker,
        Status status
) {

    public enum Status {
        WAITING,
        APPROVED,
        REJECTED,
        CANCELED
    }

}
