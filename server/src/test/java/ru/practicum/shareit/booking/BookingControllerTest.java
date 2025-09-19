package ru.practicum.shareit.booking;

import java.time.LocalDateTime;
import java.util.List;

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
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.CreateBookingRequestDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.user.model.User;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    @InjectMocks
    private BookingController bookingController;

    @Mock
    private BookingService bookingService;

    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private MockMvc mvc;

    private BookingResponseDto bookingResponseDto;

    @BeforeEach
    protected void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(bookingController).build();

        bookingResponseDto = BookingResponseDto.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .booker(User.builder()
                        .id(1L)
                        .name("petya")
                        .email("petya@mail.ru")
                        .build())
                .status(BookingStatus.APPROVED)
                .build();
    }

    @Test
    void create() throws Exception {
        Mockito.when(bookingService.create(Mockito.any(), Mockito.any())).thenReturn(bookingResponseDto);

        CreateBookingRequestDto createBookingDto = CreateBookingRequestDto.builder()
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .itemId(1L)
                .build();

        mvc.perform(MockMvcRequestBuilders.post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createBookingDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(bookingResponseDto.id()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.booker.id",
                        Matchers.is(bookingResponseDto.booker().getId()),
                        Long.class
                ));
    }

    @Test
    void approve() throws Exception {
        Mockito.when(bookingService.approve(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyBoolean()))
                .thenReturn(bookingResponseDto);

        mvc.perform(MockMvcRequestBuilders.patch("/bookings/1?approved=true").header("X-Sharer-User-Id", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(bookingResponseDto.id()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.booker.id",
                        Matchers.is(bookingResponseDto.booker().getId()),
                        Long.class
                ))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.status",
                        Matchers.is(String.valueOf(BookingStatus.APPROVED)),
                        String.class
                ));
    }

    @Test
    void getById() throws Exception {
        Mockito.when(bookingService.getById(Mockito.anyLong(), Mockito.anyLong())).thenReturn(bookingResponseDto);

        mvc.perform(MockMvcRequestBuilders.get("/bookings/1").header("X-Sharer-User-Id", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(bookingResponseDto.id()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.booker.id",
                        Matchers.is(bookingResponseDto.booker().getId()),
                        Long.class
                ))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.status",
                        Matchers.is(String.valueOf(BookingStatus.APPROVED)),
                        String.class
                ));
    }

    @Test
    void getByState() throws Exception {
        Mockito.when(bookingService.getByState(Mockito.anyLong(), Mockito.any()))
                .thenReturn(List.of(bookingResponseDto));

        mvc.perform(MockMvcRequestBuilders.get("/bookings?state=ALL").header("X-Sharer-User-Id", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(bookingResponseDto.id()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$[0].booker.id",
                        Matchers.is(bookingResponseDto.booker().getId()),
                        Long.class
                ))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$[0].status",
                        Matchers.is(String.valueOf(BookingStatus.APPROVED)),
                        String.class
                ));
    }

    @Test
    void getMyByState() throws Exception {
        Mockito.when(bookingService.getMyByState(Mockito.anyLong(), Mockito.any()))
                .thenReturn(List.of(bookingResponseDto));

        mvc.perform(MockMvcRequestBuilders.get("/bookings/owner?state=ALL").header("X-Sharer-User-Id", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(bookingResponseDto.id()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$[0].booker.id",
                        Matchers.is(bookingResponseDto.booker().getId()),
                        Long.class
                ))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$[0].status",
                        Matchers.is(String.valueOf(BookingStatus.APPROVED)),
                        String.class
                ));
    }
}