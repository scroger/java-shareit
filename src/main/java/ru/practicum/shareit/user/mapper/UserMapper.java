package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.user.dto.CreateUserRequestDto;
import ru.practicum.shareit.user.dto.UpdateUserRequestDto;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

public class UserMapper {

    public static User map(CreateUserRequestDto createUserRequestDto) {
        User user = new User();

        user.setId(null);
        user.setName(createUserRequestDto.name());
        user.setEmail(createUserRequestDto.email());

        return user;
    }

    public static User map(UpdateUserRequestDto userRequestDto, User user) {
        Optional.ofNullable(userRequestDto.name()).ifPresent(user::setName);
        Optional.ofNullable(userRequestDto.email()).ifPresent(user::setEmail);

        return user;
    }

}
