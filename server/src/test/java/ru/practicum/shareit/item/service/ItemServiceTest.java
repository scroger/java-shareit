package ru.practicum.shareit.item.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
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

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.error.exception.ForbiddenException;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.error.exception.ValidationException;
import ru.practicum.shareit.item.dto.CreateItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.UpdateItemRequestDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.impl.ItemServiceImpl;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @InjectMocks
    private ItemServiceImpl itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    private Item item;

    @BeforeEach
    void setUp() {
        item = Item.builder()
                .id(1L)
                .name("item")
                .description("description")
                .available(true)
                .owner(User.builder()
                        .id(1L)
                        .name("user")
                        .email("user@gmail.com")
                        .build())
                .build();
    }

    @Test
    void getAll() {
        Mockito.when(itemRepository.findByOwnerId(Mockito.anyLong())).thenReturn(Collections.singletonList(item));

        Collection<Item> result = itemService.getAll(1L);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(item, result.stream().toList().get(0));
        Mockito.verify(itemRepository, Mockito.times(1)).findByOwnerId(Mockito.anyLong());
    }

    @Test
    void getByIdSuccess() {
        Mockito.when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        Item result = itemService.getById(item.getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(item, result);
    }

    @Test
    void getByIdError() {
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> itemService.getById(item.getId()));
    }

    @Test
    void getByIdByOwnerShouldReturnItemWithBookingsAndComments() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastBookingDt = now.minusDays(1);
        LocalDateTime nextBookingDt = now.plusDays(1);

        Booking lastBooking = Booking.builder().end(lastBookingDt).build();
        Booking nextBooking = Booking.builder().start(nextBookingDt).build();

        Mockito.when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        Mockito.when(bookingRepository.findFirstByItemIdAndEndIsBeforeAndStatusOrderByEndDesc(
                Mockito.anyLong(), Mockito.any(), Mockito.any())).thenReturn(Optional.of(lastBooking));
        Mockito.when(bookingRepository.findFirstByItemIdAndStartIsAfterAndStatusOrderByStartAsc(
                Mockito.anyLong(), Mockito.any(), Mockito.any())).thenReturn(Optional.of(nextBooking));
        Mockito.when(commentRepository.findAllByItemId(item.getId())).thenReturn(Collections.emptyList());

        ItemResponseDto result = itemService.getById(item.getId(), item.getOwner().getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(lastBookingDt, result.lastBooking());
        Assertions.assertEquals(nextBookingDt, result.nextBooking());
        Assertions.assertNotNull(result.comments());
    }

    @Test
    void getByIdByNotOwnerShouldReturnItemWithoutBookings() {
        Mockito.when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        Mockito.when(commentRepository.findAllByItemId(item.getId())).thenReturn(Collections.emptyList());

        ItemResponseDto result = itemService.getById(item.getId(), 2L);

        Assertions.assertNotNull(result);
        Assertions.assertNull(result.lastBooking());
        Assertions.assertNull(result.nextBooking());
        Assertions.assertNotNull(result.comments());
    }

    @Test
    void createItemRequestIsNullShouldReturnSavedItem() {
        CreateItemRequestDto dto = CreateItemRequestDto.builder()
                .name("name")
                .description("description")
                .available(true)
                .build();
        User user = item.getOwner();

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(itemRepository.save(Mockito.any())).thenAnswer(invocation -> invocation.getArgument(0));

        Item result = itemService.create(user.getId(), dto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(dto.name(), result.getName());
        Assertions.assertEquals(dto.description(), result.getDescription());
        Assertions.assertEquals(dto.available(), result.getAvailable());
        Assertions.assertNull(result.getRequest());

        Mockito.verify(itemRequestRepository, Mockito.never()).findById(Mockito.anyLong());
    }

    @Test
    void createItemRequestIsNotNullShouldReturnSavedItemWithRequest() {
        CreateItemRequestDto dto = CreateItemRequestDto.builder()
                .name("name")
                .description("description")
                .available(true)
                .requestId(1L)
                .build();

        User owner = item.getOwner();
        User requester = User.builder()
                .id(2L)
                .name("requester")
                .email("requester@gmail.com")
                .build();

        ItemRequest itemRequest = ItemRequest.builder()
                .id(1L)
                .description("request description")
                .requestor(requester)
                .created(LocalDateTime.now())
                .build();

        Mockito.when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        Mockito.when(itemRequestRepository.findById(itemRequest.getId())).thenReturn(Optional.of(itemRequest));
        Mockito.when(itemRepository.save(Mockito.any())).thenAnswer(invocation -> invocation.getArgument(0));

        Item result = itemService.create(owner.getId(), dto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(dto.name(), result.getName());
        Assertions.assertEquals(dto.description(), result.getDescription());
        Assertions.assertEquals(dto.available(), result.getAvailable());
        Assertions.assertEquals(itemRequest, result.getRequest());
    }

    @Test
    void createUserNotFoundShouldThrowNotFoundException() {
        CreateItemRequestDto dto = CreateItemRequestDto.builder()
                .name("name")
                .description("description")
                .available(true)
                .requestId(null)
                .build();

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> itemService.create(1L, dto));
    }

    @Test
    void createItemRequestNotFoundShouldThrowNotFoundException() {
        CreateItemRequestDto dto = CreateItemRequestDto.builder()
                .name("name")
                .description("description")
                .available(true)
                .requestId(1L)
                .build();

        User user = item.getOwner();
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(itemRequestRepository.findById(dto.requestId())).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> itemService.create(user.getId(), dto));
    }

    @Test
    void updateByNotOwnerShouldThrowForbiddenException() {
        User user = User.builder()
                .id(2L)
                .name("user")
                .email("user@example.com")
                .build();

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        Assertions.assertThrows(ForbiddenException.class, () -> itemService.update(user.getId(), item.getId(),
                UpdateItemRequestDto.builder()
                        .name("new name")
                        .build()));
    }

    @Test
    void updateByOwnerShouldUpdateAndReturnItem() {
        UpdateItemRequestDto dto = UpdateItemRequestDto.builder()
                .name("new name")
                .build();

        Mockito.when(userRepository.findById(item.getOwner().getId())).thenReturn(Optional.of(item.getOwner()));
        Mockito.when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        Mockito.when(itemRepository.save(Mockito.any())).thenAnswer(invocation -> invocation.getArgument(0));

        Item updatedItem = itemService.update(item.getOwner().getId(), item.getId(), dto);

        Assertions.assertNotNull(updatedItem);
        Assertions.assertEquals(dto.name(), updatedItem.getName());

        Mockito.verify(itemRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void updateUserNotFoundShouldThrowNotFoundException() {
        UpdateItemRequestDto dto = UpdateItemRequestDto.builder()
                .name("new name")
                .build();

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> itemService.update(1L, 1L, dto));
    }

    @Test
    void updateItemNotFoundShouldThrowNotFoundException() {
        UpdateItemRequestDto dto = UpdateItemRequestDto.builder()
                .name("new name")
                .build();

        Mockito.when(userRepository.findById(item.getOwner().getId())).thenReturn(Optional.of(item.getOwner()));
        Mockito.when(itemRepository.findById(item.getId())).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> itemService.update(item.getOwner().getId(), item.getId(),
                dto));
    }

    @Test
    void updateItemRequestNotFoundShouldThrowNotFoundException() {
        UpdateItemRequestDto dto = UpdateItemRequestDto.builder()
                .requestId(1L)
                .build();

        Mockito.when(userRepository.findById(item.getOwner().getId())).thenReturn(Optional.of(item.getOwner()));
        Mockito.when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        Mockito.when(itemRequestRepository.findById(dto.requestId())).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> itemService.update(item.getOwner().getId(), item.getId(),
                dto));
    }

    @Test
    void searchEmpty() {
        Collection<Item> result = itemService.search("");

        Assertions.assertEquals(Collections.emptyList(), result);

        Mockito.verify(itemRepository, Mockito.never())
                .findByAvailableTrueAndNameIgnoreCaseContainingOrDescriptionIgnoreCaseContaining(
                        Mockito.anyString(),
                        Mockito.anyString()
                );
    }

    @Test
    void search() {
        List<Item> items = List.of(new Item(), new Item());

        Mockito.when(itemRepository.findByAvailableTrueAndNameIgnoreCaseContainingOrDescriptionIgnoreCaseContaining(
                "hammer",
                "hammer"
        )).thenReturn(items);

        List<Item> result = (List<Item>) itemService.search("hammer");

        Assertions.assertEquals(items, result);

        Mockito.verify(itemRepository, Mockito.times(1))
                .findByAvailableTrueAndNameIgnoreCaseContainingOrDescriptionIgnoreCaseContaining(
                        "hammer",
                        "hammer"
                );
    }

    @Test
    void addCommentSuccessful() {
        Mockito.when(userRepository.findById(item.getOwner().getId())).thenReturn(Optional.of(item.getOwner()));
        Mockito.when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        Mockito.when(bookingRepository.existsByBookerIdAndItemIdAndEndIsBeforeAndStatus(
                        Mockito.anyLong(), Mockito.anyLong(), Mockito.any(), Mockito.any()))
                .thenReturn(true);

        Comment comment = Comment.builder()
                .id(1L)
                .text("text")
                .created(LocalDate.now())
                .author(User.builder()
                        .id(2L)
                        .name("user2")
                        .email("user2@gmail.com")
                        .build())
                .item(item)
                .build();

        Mockito.when(commentRepository.save(Mockito.any())).thenAnswer(invocation -> invocation.getArgument(0));

        CommentResponseDto result = itemService.addComment(
                item.getId(),
                item.getOwner().getId(),
                CommentRequestDto.builder()
                        .text(comment.getText())
                        .build());

        Assertions.assertNotNull(result);
        Assertions.assertEquals("text", result.text());

        Mockito.verify(userRepository, Mockito.times(1)).findById(item.getOwner().getId());
        Mockito.verify(itemRepository, Mockito.times(1)).findById(item.getId());
        Mockito.verify(bookingRepository, Mockito.times(1)).existsByBookerIdAndItemIdAndEndIsBeforeAndStatus(
                Mockito.anyLong(), Mockito.anyLong(), Mockito.any(), Mockito.any());
        Mockito.verify(commentRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void testAddComment_UserNotFound() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> itemService.addComment(
                item.getId(),
                item.getOwner().getId(),
                CommentRequestDto.builder().build()
        ));

        Mockito.verify(userRepository, Mockito.times(1)).findById(Mockito.anyLong());
    }

    @Test
    void testAddComment_BookingDoesNotExist() {
        Mockito.when(userRepository.findById(item.getOwner().getId())).thenReturn(Optional.of(item.getOwner()));
        Mockito.when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        Mockito.when(bookingRepository.existsByBookerIdAndItemIdAndEndIsBeforeAndStatus(
                        Mockito.anyLong(), Mockito.anyLong(), Mockito.any(), Mockito.any()))
                .thenReturn(false);

        Assertions.assertThrows(ValidationException.class, () -> itemService.addComment(
                item.getId(),
                item.getOwner().getId(),
                CommentRequestDto.builder().build()
        ));

        Mockito.verify(userRepository, Mockito.times(1)).findById(item.getOwner().getId());
        Mockito.verify(itemRepository, Mockito.times(1)).findById(item.getId());
        Mockito.verify(bookingRepository, Mockito.times(1)).existsByBookerIdAndItemIdAndEndIsBeforeAndStatus(
                Mockito.anyLong(), Mockito.anyLong(), Mockito.any(), Mockito.any());
    }
}