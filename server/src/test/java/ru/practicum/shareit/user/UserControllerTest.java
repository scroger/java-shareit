package ru.practicum.shareit.user;

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

import ru.practicum.shareit.user.dto.CreateUserRequestDto;
import ru.practicum.shareit.user.dto.UpdateUserRequestDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;

    private User user;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(userController).build();

        user = User.builder()
                .id(1L)
                .name("name")
                .email("email@email.com")
                .build();
    }

    @Test
    void getAllTest() throws Exception {
        Mockito.when(userService.getAll()).thenReturn(Collections.singletonList(user));

        mvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(user.getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is(user.getName()), String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email", Matchers.is(user.getEmail()), String.class));
    }

    @Test
    void getByIdTest() throws Exception {
        Mockito.when(userService.getById(1L)).thenReturn(user);

        mvc.perform(MockMvcRequestBuilders.get("/users/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(user.getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(user.getName()), String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is(user.getEmail()), String.class));
    }

    @Test
    void createTest() throws Exception {
        Mockito.when(userService.create(Mockito.any())).thenReturn(user);

        mvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(CreateUserRequestDto.builder()
                                .name("name")
                                .email("email@email.com")
                                .build())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(user.getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(user.getName()), String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is(user.getEmail()), String.class));
    }

    @Test
    void updateTest() throws Exception {
        Mockito.when(userService.update(Mockito.anyLong(), Mockito.any())).thenReturn(user);

        mvc.perform(MockMvcRequestBuilders.patch("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(UpdateUserRequestDto.builder()
                        .name("name")
                        .email("email@email.com")
                        .build())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(user.getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(user.getName()), String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is(user.getEmail()), String.class));
    }

    @Test
    void deleteTest() throws Exception {
        Mockito.doNothing().when(userService).delete(Mockito.anyLong());

        mvc.perform(MockMvcRequestBuilders.delete("/users/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}
