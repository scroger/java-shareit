package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.CreateBookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {

    public static Booking map(CreateBookingRequestDto bookingRequest, Item item, User user) {
        Booking booking = new Booking();

        booking.setId(null);
        booking.setStart(bookingRequest.start());
        booking.setEnd(bookingRequest.end());
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(Booking.Status.WAITING);

        return booking;
    }

    public static BookingResponseDto map(Booking booking) {
        return BookingResponseDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
//                .itemId(booking.getItem().getId())
//                .bookerId(booking.getBooker().getId())
                .item(booking.getItem())
                .booker(booking.getBooker())
                .status(booking.getStatus())
                .build();
    }

}
