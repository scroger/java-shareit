package ru.practicum.shareit.booking.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.BookingStateDto;
import ru.practicum.shareit.booking.dto.CreateBookingRequestDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.impl.BookingServiceImpl;
import ru.practicum.shareit.error.exception.ForbiddenException;
import ru.practicum.shareit.error.exception.ItemNotAvailableException;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.error.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemService itemService;

    @Mock
    private UserService userService;

    private User user;
    private Item item;
    private Booking booking;
    private BookingResponseDto bookingResponseDto;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("user")
                .email("user@example.com")
                .build();

        item = Item.builder()
                .id(1L)
                .name("item")
                .available(true)
                .owner(user)
                .build();

        booking = Booking.builder()
                .id(1L)
                .item(item)
                .booker(user)
                .status(BookingStatus.WAITING)
                .start(LocalDateTime.now().minusDays(2))
                .end(LocalDateTime.now().minusDays(1))
                .build();

        bookingResponseDto = BookingMapper.map(booking);
    }

    @Test
    void createSuccess() {
        Mockito.when(itemService.getById(item.getId())).thenReturn(item);
        Mockito.when(userService.getById(user.getId())).thenReturn(user);
        Mockito.when(bookingRepository.save(Mockito.any())).thenReturn(booking);

        BookingResponseDto result = bookingService.create(CreateBookingRequestDto.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .itemId(item.getId())
                .build(), user.getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(bookingResponseDto, result);

        Mockito.verify(itemService).getById(item.getId());
        Mockito.verify(userService).getById(user.getId());
        Mockito.verify(bookingRepository).save(Mockito.any());
    }

    @Test
    void createItemNotAvailable() {
        item.setAvailable(false);

        Mockito.when(itemService.getById(item.getId())).thenReturn(item);

        Assertions.assertThrows(ItemNotAvailableException.class, () -> bookingService.create(CreateBookingRequestDto.builder()
                .itemId(item.getId())
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build(), user.getId()));
    }

    @Test
    void approveByOwner() {
        Mockito.when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        Mockito.when(bookingRepository.save(Mockito.any())).thenReturn(booking);

        BookingResponseDto result = bookingService.approve(user.getId(), booking.getId(), true);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(BookingStatus.APPROVED, result.status());

        Mockito.verify(bookingRepository).findById(booking.getId());
        Mockito.verify(bookingRepository).save(Mockito.any());
    }

    @Test
    void approveByNotOwner() {
        Mockito.when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        Assertions.assertThrows(ValidationException.class, () -> bookingService.approve(2L, booking.getId(), true));
    }

    @Test
    void getByIdByBookerOrOwner() {
        Mockito.when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        BookingResponseDto result = bookingService.getById(booking.getId(), user.getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(bookingResponseDto, result);

        Mockito.verify(bookingRepository).findById(booking.getId());
    }

    @Test
    void getByIdByNotBookerOrOwner() {
        Mockito.when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        Assertions.assertThrows(ForbiddenException.class, () -> bookingService.getById(booking.getId(), 2L));
    }

    @Test
    void getByIdNotFound() {
        Mockito.when(bookingRepository.findById(booking.getId())).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> bookingService.getById(booking.getId(), user.getId()));
    }

    @Test
    void getByState() {
        List<Booking> bookings = List.of(booking);
        Mockito.when(bookingRepository.findAllByBookerIdOrderByStartDesc(user.getId())).thenReturn(bookings);

        List<BookingResponseDto> result = (List<BookingResponseDto>) bookingService.getByState(user.getId(),
                BookingStateDto.ALL);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(bookingResponseDto, result.get(0));

        Mockito.verify(bookingRepository).findAllByBookerIdOrderByStartDesc(user.getId());
    }

    @Test
    void getMyByStateSuccess() {
        List<Item> items = List.of(item);
        Mockito.when(itemService.getAll(user.getId())).thenReturn(items);
        List<Booking> bookings = List.of(booking);
        Mockito.when(bookingRepository.findAllByItemOwnerIdOrderByStartDesc(user.getId())).thenReturn(bookings);

        List<BookingResponseDto> result = (List<BookingResponseDto>) bookingService.getMyByState(user.getId(), BookingStateDto.ALL);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(bookingResponseDto, result.get(0));

        Mockito.verify(itemService).getAll(user.getId());
        Mockito.verify(bookingRepository).findAllByItemOwnerIdOrderByStartDesc(user.getId());
    }

    @Test
    void getMyByStateEmpty() {
        Mockito.when(itemService.getAll(user.getId())).thenReturn(Collections.emptyList());

        Assertions.assertThrows(NotFoundException.class, () -> bookingService.getMyByState(user.getId(), BookingStateDto.ALL));
    }
}