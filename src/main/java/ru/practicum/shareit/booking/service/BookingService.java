package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.BookingStateDto;
import ru.practicum.shareit.booking.dto.CreateBookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.Collection;

public interface BookingService {

    BookingResponseDto create(CreateBookingRequestDto bookingRequest, Long userId);

    BookingResponseDto approve(Long userId, Long bookingId, boolean approved);

    Booking getById(Long bookingId);

    BookingResponseDto getById(Long bookingId, Long userId);

    Collection<BookingResponseDto> getByState(Long userId, BookingStateDto state);

    Collection<BookingResponseDto> getMyByState(Long userId, BookingStateDto state);

}
