package ru.practicum.shareit.booking;

import java.util.Collection;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.BookingStateDto;
import ru.practicum.shareit.booking.dto.CreateBookingRequestDto;
import ru.practicum.shareit.booking.service.BookingService;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingResponseDto create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @RequestBody CreateBookingRequestDto bookingRequest) {
        return bookingService.create(bookingRequest, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto approve(@RequestHeader("X-Sharer-User-Id") Long userId,
                                      @PathVariable("bookingId") Long bookingId,
                                      @RequestParam(value = "approved") boolean approved) {
        return bookingService.approve(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                      @PathVariable("bookingId") Long bookingId) {
        return bookingService.getById(bookingId, userId);
    }

    @GetMapping
    public Collection<BookingResponseDto> getByState(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(value = "state", defaultValue = "ALL") BookingStateDto state,
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        return bookingService.getByState(userId, state); //TODO add from and size
    }

    @GetMapping("/owner")
    public Collection<BookingResponseDto> getMyByState(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(value = "state", defaultValue = "ALL") BookingStateDto state
    ) {
        return bookingService.getMyByState(userId, state);
    }

}
