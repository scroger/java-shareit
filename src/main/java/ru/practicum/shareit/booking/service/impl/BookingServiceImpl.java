package ru.practicum.shareit.booking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.BookingStateDto;
import ru.practicum.shareit.booking.dto.CreateBookingRequestDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.error.exception.ForbiddenException;
import ru.practicum.shareit.error.exception.ItemNotAvailableException;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.error.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    private final ItemService itemService;

    private final UserService userService;

    @Override
    public BookingResponseDto create(CreateBookingRequestDto bookingRequest, Long userId) {
        if (bookingRequest.start().isAfter(bookingRequest.end()) || bookingRequest.start().isEqual(bookingRequest.end())) {
            throw new ValidationException("Invalid start/end date");
        }

        Item item = itemService.getById(bookingRequest.itemId());
        if (!item.getAvailable()) {
            throw new ItemNotAvailableException("Item isn't available");
        }

        User user = userService.getById(userId);

        return BookingMapper.map(
                bookingRepository.save(
                        BookingMapper.map(bookingRequest, item, user)
                )
        );
    }

    @Override
    public BookingResponseDto approve(Long userId, Long bookingId, boolean approved) {
        Booking booking = getById(bookingId);

        if (!Objects.equals(userId, booking.getItem().getOwner().getId())) {
            throw new ValidationException("Only the owner can approve booking");
        }

        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);

        return BookingMapper.map(bookingRepository.save(booking));
    }

    public Booking getById(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String.format("Booking with id=%d not found", bookingId)));
    }

    @Override
    public BookingResponseDto getById(Long bookingId, Long userId) {
        Booking booking = getById(bookingId);

        if (!Objects.equals(userId, booking.getBooker().getId())
                && !Objects.equals(userId, booking.getItem().getOwner().getId())) {
            throw new ForbiddenException("Only the owner of item or the booker can view this booking");
        }

        return BookingMapper.map(booking);
    }

    @Override
    public Collection<BookingResponseDto> getByState(Long userId, BookingStateDto state) {
        LocalDate now = LocalDate.now();

        Collection<Booking> bookings = switch (state) {
            case BookingStateDto.ALL -> bookingRepository.findAllByBookerIdOrderByStartDesc(userId);

            case BookingStateDto.CURRENT ->
                    bookingRepository.findAllByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(userId, now, now);

            case BookingStateDto.PAST -> bookingRepository.findAllByBookerIdAndEndIsBeforeOrderByStartDesc(userId, now);

            case BookingStateDto.FUTURE ->
                    bookingRepository.findAllByBookerIdAndStartIsAfterOrderByStartDesc(userId, now);

            default -> bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, state);
        };

        return bookings.stream().map(BookingMapper::map).toList();
    }

    @Override
    public Collection<BookingResponseDto> getMyByState(Long userId, BookingStateDto state) {
        Collection<Item> items = itemService.getAll(userId);
        if (items.isEmpty()) {
            throw new NotFoundException("User don't have any items");
        }

        Collection<Booking> bookings = switch (state) {
            case BookingStateDto.ALL -> bookingRepository.findAllByItemOwnerIdOrderByStartDesc(userId);

            case BookingStateDto.CURRENT ->
                    bookingRepository.findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(userId,
                            LocalDate.now(), LocalDate.now());

            case BookingStateDto.PAST ->
                    bookingRepository.findAllByItemOwnerIdAndEndIsBeforeOrderByStartDesc(userId, LocalDate.now());

            case BookingStateDto.FUTURE ->
                    bookingRepository.findAllByItemOwnerIdAndStartIsAfterOrderByStartDesc(userId, LocalDate.now());

            default -> bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, state);
        };

        return bookings.stream().map(BookingMapper::map).toList();
    }
}
