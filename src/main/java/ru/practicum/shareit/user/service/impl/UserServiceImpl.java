package ru.practicum.shareit.user.service.impl;

import java.util.Collection;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.error.exception.AlreadyExistException;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.user.dto.CreateUserRequestDto;
import ru.practicum.shareit.user.dto.UpdateUserRequestDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    public final UserRepository userRepository;

    @Override
    public Collection<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User getById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id=%d not found", userId)));
    }

    @Override
    public User create(CreateUserRequestDto createUserRequestDto) {
        if (userRepository.existsByEmail(createUserRequestDto.email())) {
            throw new AlreadyExistException(String.format("User with email=%s already exist",
                    createUserRequestDto.email()));
        }

        return userRepository.create(UserMapper.map(createUserRequestDto));
    }

    @Override
    public User update(Long userId, UpdateUserRequestDto updateUserRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id=%d not found", userId)));

        String newEmail = updateUserRequestDto.email();
        if (null != newEmail && !user.email().equalsIgnoreCase(newEmail) && userRepository.existsByEmail(newEmail)) {
            throw new AlreadyExistException(String.format("User with email=%s already exist", newEmail));
        }

        return userRepository.update(UserMapper.map(updateUserRequestDto, user));
    }

    @Override
    public void delete(Long userId) {
        userRepository.delete(userId);
    }

}
