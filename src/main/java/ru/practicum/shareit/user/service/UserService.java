package ru.practicum.shareit.user.service;

import java.util.Collection;

import ru.practicum.shareit.user.dto.CreateUserRequestDto;
import ru.practicum.shareit.user.dto.UpdateUserRequestDto;
import ru.practicum.shareit.user.model.User;

public interface UserService {

    Collection<User> getAll();

    User getById(Long userId);

    User create(CreateUserRequestDto createUserRequestDto);

    User update(Long userId, UpdateUserRequestDto updateUserRequestDto);

    void delete(Long userId);

}
