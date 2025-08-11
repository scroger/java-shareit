package ru.practicum.shareit.user.repository.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Repository
@RequiredArgsConstructor
public class InMemoryUserRepository implements UserRepository {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Boolean existsByEmail(String email) {
        return users.values()
                .stream()
                .anyMatch(user -> user.email().equalsIgnoreCase(email));
    }

    @Override
    public User create(User user) {
        user = user.withId(nextId());

        users.put(user.id(), user);

        return user;
    }

    @Override
    public User update(User user) {
        users.replace(user.id(), user);

        return user;
    }

    @Override
    public void delete(Long id) {
        users.remove(id);
    }

    private Long nextId() {
        return users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0) + 1;
    }

}
