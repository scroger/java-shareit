package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingResponseDtoTest {
    private final JacksonTester<BookingResponseDto> json;

    @Test
    void testBookingResponseDto() throws Exception {
        User booker = User.builder()
                .id(1L)
                .name("Booker name")
                .email("booker@booker.com")
                .build();
        User itemOwner = User.builder()
                .id(2L)
                .name("Item owner name")
                .email("owner@owner.com")
                .build();
        ItemRequest itemRequest = ItemRequest.builder()
                .id(1L)
                .description("Item request description")
                .requestorId(booker.getId())
                .requestor(booker)
                .created(LocalDateTime.of(2025, 1, 2, 3, 4, 5))
                .build();
        Item item = Item.builder()
                .id(1L)
                .name("Item name")
                .description("Item description")
                .available(true)
                .owner(itemOwner)
                .request(itemRequest)
                .build();
        BookingResponseDto bookingResponseDto = BookingResponseDto.builder()
                .id(1L)
                .start(LocalDateTime.of(2025, 1, 2, 3, 4, 5))
                .end(LocalDateTime.of(2025, 1, 2, 3, 4, 5))
                .item(item)
                .booker(booker)
                .status(BookingStatus.REJECTED)
                .build();
        JsonContent<BookingResponseDto> result = json.write(bookingResponseDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2025-01-02T03:04:05");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2025-01-02T03:04:05");
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo("Item name");
        assertThat(result).extractingJsonPathStringValue("$.item.description").isEqualTo("Item description");
        assertThat(result).extractingJsonPathBooleanValue("$.item.available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.item.owner.id").isEqualTo(2);
        assertThat(result).extractingJsonPathStringValue("$.item.owner.name").isEqualTo("Item owner name");
        assertThat(result).extractingJsonPathStringValue("$.item.owner.email").isEqualTo("owner@owner.com");
        assertThat(result).extractingJsonPathNumberValue("$.item.request.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.item.request.description")
                .isEqualTo("Item request description");
        assertThat(result).extractingJsonPathNumberValue("$.item.request.requestorId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.item.request.created").isEqualTo("2025-01-02T03:04:05");
        assertThat(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.booker.name").isEqualTo("Booker name");
        assertThat(result).extractingJsonPathStringValue("$.booker.email").isEqualTo("booker@booker.com");
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("REJECTED");

        // test @JsonIgnore
        assertThat(result).doesNotHaveJsonPath("$.item.request.requestor");
    }
}