package ru.practicum.shareit.request.service;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.request.dto.CreateItemRequestRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.CreateUserRequestDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestServiceIntegrationTest {

    private final EntityManager em;
    private final UserService userService;
    private final ItemRequestService itemRequestService;

    @Test
    void testCreateItemRequest() {
        User requestor = userService.create(CreateUserRequestDto.builder()
                .name("test")
                .email("test@test.ru")
                .build());

        String description = "description";
        itemRequestService.create(new CreateItemRequestRequestDto(description), requestor.getId());
        TypedQuery<ItemRequest> query = em.createQuery("select ir from ItemRequest ir where ir.description = :description", ItemRequest.class);
        ItemRequest itemRequest = query.setParameter("description", description).getSingleResult();

        MatcherAssert.assertThat(itemRequest, Matchers.notNullValue());
        MatcherAssert.assertThat(itemRequest.getRequestor().getId(), Matchers.equalTo(requestor.getId()));
    }

}