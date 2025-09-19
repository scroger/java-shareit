package ru.practicum.shareit.item.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.comment.dto.CommentResponseDto;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemResponseDtoTest {
    private final JacksonTester<ItemResponseDto> json;

    @Test
    void testItemResponseDto() throws Exception {
        JsonContent<ItemResponseDto> result = json.write(ItemResponseDto.builder()
                .id(1L)
                .name("Item name")
                .description("Item description")
                .available(true)
                .ownerId(1L)
                .lastBooking(LocalDateTime.of(2024, 1, 2, 3, 4, 5))
                .nextBooking(LocalDateTime.of(2025, 1, 2, 3, 4, 5))
                .comments(Collections.singletonList(CommentResponseDto.builder()
                        .id(1L)
                        .text("Comment text")
                        .authorName("Author name")
                        .created(LocalDate.of(2024, 1, 2))
                        .build()))
                .build());

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Item name");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Item description");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.ownerId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.lastBooking").isEqualTo("2024-01-02T03:04:05");
        assertThat(result).extractingJsonPathStringValue("$.nextBooking").isEqualTo("2025-01-02T03:04:05");
        assertThat(result).extractingJsonPathNumberValue("$.comments[0].id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.comments[0].text").isEqualTo("Comment text");
        assertThat(result).extractingJsonPathStringValue("$.comments[0].authorName").isEqualTo("Author name");
        assertThat(result).extractingJsonPathStringValue("$.comments[0].created").isEqualTo("2024-01-02");
    }
}