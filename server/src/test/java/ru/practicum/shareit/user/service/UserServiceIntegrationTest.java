package ru.practicum.shareit.user.service;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.user.dto.CreateUserRequestDto;
import ru.practicum.shareit.user.model.User;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceIntegrationTest {

    private final EntityManager em;
    private final UserService userService;

    @Test
    void testCreateUser() {
        String email = "test@test.ru";
        userService.create(CreateUserRequestDto.builder()
                .name("test")
                .email(email)
                .build());
        TypedQuery<User> userQuery = em.createQuery("select u from User u where u.email = :email", User.class);
        User user = userQuery.setParameter("email", email).getSingleResult();

        MatcherAssert.assertThat(user, Matchers.notNullValue());
        MatcherAssert.assertThat(user.getId(), Matchers.notNullValue());
        MatcherAssert.assertThat(user.getName(), Matchers.equalTo("test"));
    }

}