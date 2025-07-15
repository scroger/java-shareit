package ru.practicum.shareit.user.repository;

import java.util.Collection;
import java.util.Optional;

import ru.practicum.shareit.user.model.User;

public interface UserRepository {

    Collection<User> findAll();

    Optional<User> findById(Long id);

    Boolean existsByEmail(String email);

    User create(User user);

    User update(User user);

    void delete(Long id);

}
