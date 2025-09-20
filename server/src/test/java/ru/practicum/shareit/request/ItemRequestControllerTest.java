package ru.practicum.shareit.request;

import java.time.LocalDateTime;
import java.util.Collections;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import ru.practicum.shareit.request.dto.CreateItemRequestRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;

@ExtendWith(MockitoExtension.class)
class ItemRequestControllerTest {

    @Mock
    private ItemRequestService itemRequestService;

    @InjectMocks
    private ItemRequestController itemRequestController;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;

    private ItemRequest itemRequest;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(itemRequestController).build();

        itemRequest = ItemRequest.builder()
                .id(1L)
                .description("description")
                .requestorId(1L)
                .requestor(User.builder()
                        .id(1L)
                        .name("name")
                        .email("email@email.com")
                        .build())
                .created(LocalDateTime.now())
                .build();
    }

    @Test
    void getMyRequests() throws Exception {
        Mockito.when(itemRequestService.getMyRequests(1L))
                .thenReturn(Collections.singletonList(ItemRequestMapper.map(itemRequest, Collections.emptyList())));

        mvc.perform(MockMvcRequestBuilders.get("/requests").header("X-Sharer-User-Id", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(itemRequest.getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description", Matchers.is(itemRequest.getDescription()),
                        String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].requestorId", Matchers.is(itemRequest.getRequestorId()),
                        Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].items", Matchers.hasSize(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].created", Matchers.notNullValue()));
    }

    @Test
    void getAllRequests() throws Exception {
        Mockito.when(itemRequestService.getAllRequests(1L)).thenReturn(Collections.singletonList(itemRequest));

        mvc.perform(MockMvcRequestBuilders.get("/requests/all").header("X-Sharer-User-Id", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(itemRequest.getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description", Matchers.is(itemRequest.getDescription()),
                        String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].requestorId", Matchers.is(itemRequest.getRequestorId()),
                        Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].requestor").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].created", Matchers.notNullValue()));
    }

    @Test
    void getById() throws Exception {
        Mockito.when(itemRequestService.getById(1L)).thenReturn(ItemRequestMapper.map(itemRequest, Collections.emptyList()));

        mvc.perform(MockMvcRequestBuilders.get("/requests/1").header("X-Sharer-User-Id", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(itemRequest.getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is(itemRequest.getDescription()),
                        String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.requestorId", Matchers.is(itemRequest.getRequestorId()),
                        Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.created", Matchers.notNullValue()));
    }

    @Test
    void create() throws Exception {
        Mockito.when(itemRequestService.create(Mockito.any(), Mockito.anyLong())).thenReturn(itemRequest);

        mvc.perform(MockMvcRequestBuilders.post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new CreateItemRequestRequestDto("description"))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(itemRequest.getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is(itemRequest.getDescription()),
                        String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.requestorId", Matchers.is(itemRequest.getRequestorId()),
                        Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.requestor").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.created", Matchers.notNullValue()));
    }

}