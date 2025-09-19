package ru.practicum.shareit.request.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.CreateItemRequestRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.impl.ItemRequestServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceTest {

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    private User user;
    private ItemRequest itemRequest;
    private Item item;
    private final CreateItemRequestRequestDto createItemRequestRequestDto = new CreateItemRequestRequestDto("description");

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("user")
                .email("user@example.com")
                .build();

        itemRequest = ItemRequest.builder()
                .id(1L)
                .description("description")
                .requestor(user)
                .created(LocalDateTime.now())
                .build();

        item = Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .request(itemRequest)
                .owner(user)
                .build();
    }

    @Test
    void getMyRequests() {
        Mockito.when(itemRequestRepository.findAllByRequestorId(user.getId())).thenReturn(List.of(itemRequest));
        Mockito.when(itemRepository.findAllByRequestIdIn(Set.of(itemRequest.getId()))).thenReturn(List.of(item));

        List<ItemRequestResponseDto> result = (List<ItemRequestResponseDto>) itemRequestService.getMyRequests(
                user.getId());

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(itemRequest.getDescription(), result.get(0).description());
    }

    @Test
    void getAllRequests() {
        Mockito.when(itemRequestRepository.findAllByRequestorIdNot(user.getId())).thenReturn(List.of(itemRequest));

        List<ItemRequest> result = (List<ItemRequest>) itemRequestService.getAllRequests(user.getId());

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(itemRequest.getDescription(), result.get(0).getDescription());
    }

    @Test
    void getByIdItemRequestNotFound() {
        Mockito.when(itemRequestRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> itemRequestService.getById(1L));
    }

    @Test
    void getByIdItemRequestFound() {
        Mockito.when(itemRequestRepository.findById(itemRequest.getId())).thenReturn(Optional.of(itemRequest));
        Mockito.when(itemRepository.findAllByRequestId(itemRequest.getId())).thenReturn(List.of(item));

        ItemRequestResponseDto result = itemRequestService.getById(itemRequest.getId());

        Assertions.assertEquals(itemRequest.getDescription(), result.description());
    }

    @Test
    void createSuccess() {
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(itemRequestRepository.save(Mockito.any(ItemRequest.class))).thenReturn(itemRequest);

        ItemRequest result = itemRequestService.create(createItemRequestRequestDto, user.getId());

        Assertions.assertEquals(itemRequest.getDescription(), result.getDescription());
        Assertions.assertEquals(user, result.getRequestor());
    }

    @Test
    void testCreate_UserNotFound() {
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> itemRequestService.create(createItemRequestRequestDto,
                user.getId()));
    }
}