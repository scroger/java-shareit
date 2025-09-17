package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.CreateBookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {

    public static Booking map(CreateBookingRequestDto bookingRequest, Item item, User user) {
        return Booking.builder()
                .start(bookingRequest.start())
                .end(bookingRequest.end())
                .item(item)
                .booker(user)
                .status(BookingStatus.WAITING)
                .build();
    }

    public static BookingResponseDto map(Booking booking) {
        return BookingResponseDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(booking.getItem())
                .booker(booking.getBooker())
                .status(booking.getStatus())
                .build();
    }

}
