package ru.practicum.shareit.request.dto;

import java.time.LocalDateTime;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestResponseDtoTest {
    private final JacksonTester<ItemRequestResponseDto> json;

    @Test
    void testItemRequestResponseDto() throws Exception {
        User requestor = User.builder()
                .id(1L)
                .name("requestorName")
                .email("requestor@mail.ru")
                .build();
        ItemRequest itemRequest = ItemRequest.builder()
                .id(1L)
                .description("description")
                .requestorId(requestor.getId())
                .requestor(requestor)
                .created(LocalDateTime.now())
                .build();
        User owner = User.builder()
                .id(2L)
                .name("ownerName")
                .email("owner@mail.ru")
                .build();
        Item item = Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .owner(owner)
                .request(itemRequest)
                .build();
        JsonContent<ItemRequestResponseDto> result = json.write(ItemRequestResponseDto.builder()
                .id(1L)
                .description("description")
                .requestorId(requestor.getId())
                .items(Collections.singletonList(item))
                .created(LocalDateTime.now())
                .build());


        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(itemRequest.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(itemRequest.getDescription());
        assertThat(result).extractingJsonPathNumberValue("$.requestorId").isEqualTo(requestor.getId().intValue());
        assertThat(result).extractingJsonPathNumberValue("$.items[0].id").isEqualTo(item.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.items[0].name").isEqualTo(item.getName());
        assertThat(result).extractingJsonPathStringValue("$.items[0].description").isEqualTo(item.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.items[0].available").isEqualTo(item.getAvailable());
        assertThat(result).extractingJsonPathNumberValue("$.items[0].owner.id").isEqualTo(owner.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.items[0].owner.name").isEqualTo(owner.getName());
        assertThat(result).extractingJsonPathStringValue("$.items[0].owner.email").isEqualTo(owner.getEmail());
        assertThat(result).extractingJsonPathNumberValue("$.items[0].request.id").isEqualTo(itemRequest.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.items[0].request.description")
                .isEqualTo(itemRequest.getDescription());
        assertThat(result).extractingJsonPathNumberValue("$.items[0].request.requestorId")
                .isEqualTo(itemRequest.getRequestorId().intValue());
        assertThat(result).doesNotHaveJsonPath("$.items[0].request.requestor");
        assertThat(result).extractingJsonPathStringValue("$.items[0].request.created").isNotNull();
        assertThat(result).extractingJsonPathStringValue("$.created").isNotNull();
    }
}