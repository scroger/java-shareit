package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.user.dto.CreateUserRequestDto;
import ru.practicum.shareit.user.dto.UpdateUserRequestDto;
import ru.practicum.shareit.user.model.User;

public class UserMapper {

    public static User map(CreateUserRequestDto createUserRequestDto) {
        return User.builder()
                .id(null)
                .name(createUserRequestDto.name())
                .email(createUserRequestDto.email())
                .build();
    }

    public static User map(UpdateUserRequestDto userRequestDto, User user) {
        return user.toBuilder()
                .name(null != userRequestDto.name() ? userRequestDto.name() : user.name())
                .email(null != userRequestDto.email() ? userRequestDto.email() : user.email())
                .build();
    }

}
