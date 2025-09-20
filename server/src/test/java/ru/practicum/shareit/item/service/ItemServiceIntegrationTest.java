package ru.practicum.shareit.item.service;

import java.util.List;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.item.dto.CreateItemRequestDto;
import ru.practicum.shareit.item.dto.UpdateItemRequestDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.CreateUserRequestDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceIntegrationTest {

    private final EntityManager em;
    private final ItemService itemService;
    private final UserService userService;

    @Test
    void testCreateItem() {
        User user = userService.create(CreateUserRequestDto.builder()
                .name("test")
                .email("test@test.ru")
                .build());

        String itemName = "test";
        itemService.create(user.getId(), CreateItemRequestDto.builder()
                .name(itemName)
                .description("test")
                .available(true)
                .build());
        TypedQuery<Item> queryItem = em.createQuery("select i from Item i where i.name = :name", Item.class);
        Item item = queryItem.setParameter("name", itemName).getSingleResult();

        MatcherAssert.assertThat(item, Matchers.notNullValue());
        MatcherAssert.assertThat(item.getDescription(), Matchers.equalTo("test"));
        MatcherAssert.assertThat(item.getAvailable(), Matchers.equalTo(true));

        itemService.update(user.getId(), item.getId(), UpdateItemRequestDto.builder()
                .name("test2")
                .build());

        MatcherAssert.assertThat(item.getName(), Matchers.equalTo("test2"));

        List<Item> foundItem = itemService.search("test2").stream().toList();

        MatcherAssert.assertThat(foundItem, Matchers.notNullValue());
        MatcherAssert.assertThat(foundItem, Matchers.hasSize(1));
        MatcherAssert.assertThat(foundItem.get(0), Matchers.equalTo(item));
    }
}