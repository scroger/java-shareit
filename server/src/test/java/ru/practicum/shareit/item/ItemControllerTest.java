package ru.practicum.shareit.item;

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

import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.error.GlobalExceptionHandler;
import ru.practicum.shareit.error.exception.ForbiddenException;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.error.exception.ValidationException;
import ru.practicum.shareit.item.dto.CreateItemRequestDto;
import ru.practicum.shareit.item.dto.UpdateItemRequestDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {

    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController itemController;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;

    private Item item;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(itemController)
                .setControllerAdvice(GlobalExceptionHandler.class)
                .build();

        item = Item.builder()
                .id(1L)
                .name("item")
                .description("description")
                .available(true)
                .owner(User.builder()
                        .id(1L)
                        .name("user")
                        .email("user@mail.ru")
                        .build())
                .request(null)
                .build();
    }

    @Test
    void getAll() throws Exception {
        Mockito.when(itemService.getAll(Mockito.anyLong())).thenReturn(Collections.singletonList(item));

        mvc.perform(MockMvcRequestBuilders.get("/items").header("X-Sharer-User-Id", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(item.getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is(item.getName()), String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description", Matchers.is(item.getDescription()),
                        String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].available", Matchers.is(item.getAvailable()),
                        Boolean.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].owner.id", Matchers.is(item.getOwner().getId()),
                        Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].owner.name", Matchers.is(item.getOwner().getName()),
                        String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].owner.email", Matchers.is(item.getOwner().getEmail()),
                        String.class));
    }

    @Test
    void getById() throws Exception {
        Mockito.when(itemService.getById(Mockito.anyLong(), Mockito.anyLong())).thenReturn(ItemMapper.map(item));

        mvc.perform(MockMvcRequestBuilders.get("/items/1").header("X-Sharer-User-Id", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(item.getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(item.getName()), String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is(item.getDescription()),
                        String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.available", Matchers.is(item.getAvailable()),
                        Boolean.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.ownerId", Matchers.is(item.getOwner().getId()),
                        Long.class));
    }

    @Test
    void getByIdNotFound() throws Exception {
        Mockito.when(itemService.getById(Mockito.anyLong(), Mockito.anyLong()))
                .thenThrow(new NotFoundException("Item not found"));

        mvc.perform(MockMvcRequestBuilders.get("/items/1").header("X-Sharer-User-Id", 1L))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("Item not found"), String.class));
    }

    @Test
    void create() throws Exception {
        Mockito.when(itemService.create(Mockito.anyLong(), Mockito.any())).thenReturn(item);

        mvc.perform(MockMvcRequestBuilders.post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(CreateItemRequestDto.builder()
                                .name(item.getName())
                                .description(item.getDescription())
                                .available(item.getAvailable())
                                .build())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(item.getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(item.getName()), String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is(item.getDescription()),
                        String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.available", Matchers.is(item.getAvailable()),
                        Boolean.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.owner.id", Matchers.is(item.getOwner().getId()),
                        Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.owner.name", Matchers.is(item.getOwner().getName()),
                        String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.owner.email", Matchers.is(item.getOwner().getEmail()),
                        String.class));
    }

    @Test
    void update() throws Exception {
        Mockito.when(itemService.update(Mockito.anyLong(), Mockito.anyLong(), Mockito.any())).thenReturn(item);

        mvc.perform(MockMvcRequestBuilders.patch("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(UpdateItemRequestDto.builder()
                                .name("item")
                                .description("description")
                                .available(false)
                                .build())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(item.getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(item.getName()), String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is(item.getDescription()),
                        String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.available", Matchers.is(item.getAvailable()),
                        Boolean.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.owner.id", Matchers.is(item.getOwner().getId()),
                        Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.owner.name", Matchers.is(item.getOwner().getName()),
                        String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.owner.email", Matchers.is(item.getOwner().getEmail()),
                        String.class));
    }

    @Test
    void updateForbidden() throws Exception {
        Mockito.when(itemService.update(Mockito.anyLong(), Mockito.anyLong(), Mockito.any()))
                .thenThrow(new ForbiddenException("Forbidden"));

        mvc.perform(MockMvcRequestBuilders.patch("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(UpdateItemRequestDto.builder()
                                .name("item")
                                .description("description")
                                .available(false)
                                .build())))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("Forbidden"), String.class));
    }

    @Test
    void search() throws Exception {
        Mockito.when(itemService.search(Mockito.anyString())).thenReturn(Collections.emptyList());

        mvc.perform(MockMvcRequestBuilders.get("/items/search").header("X-Sharer-User-Id", 1L))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());

        mvc.perform(MockMvcRequestBuilders.get("/items/search?text=test").header("X-Sharer-User-Id", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    void addComment() throws Exception {
        Mockito.when(itemService.addComment(Mockito.anyLong(), Mockito.anyLong(), Mockito.any(CommentRequestDto.class)))
                .thenReturn(CommentResponseDto.builder()
                        .id(1L)
                        .text("comment")
                        .authorName("user")
                        .build());

        mvc.perform(MockMvcRequestBuilders.post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(CommentRequestDto.builder().text("comment").build())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1L), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text", Matchers.is("comment"), String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorName", Matchers.is("user"), String.class));
    }

    @Test
    void addCommentForbiddenAsBadRequest() throws Exception {
        Mockito.when(itemService.addComment(Mockito.anyLong(), Mockito.anyLong(), Mockito.any()))
                .thenThrow(new ValidationException("Forbidden"));

        mvc.perform(MockMvcRequestBuilders.post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(CommentRequestDto.builder().text("comment").build())))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("Forbidden"), String.class));
    }
}