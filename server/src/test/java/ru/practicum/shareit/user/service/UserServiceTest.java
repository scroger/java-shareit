package ru.practicum.shareit.user.service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.practicum.shareit.error.exception.AlreadyExistException;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.user.dto.CreateUserRequestDto;
import ru.practicum.shareit.user.dto.UpdateUserRequestDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.impl.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    private User user;
    private CreateUserRequestDto createUserRequestDto;
    private UpdateUserRequestDto updateUserRequestDto;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("user")
                .email("user@example.com")
                .build();

        createUserRequestDto = CreateUserRequestDto.builder()
                .name("user")
                .email("user@example.com")
                .build();

        updateUserRequestDto = UpdateUserRequestDto.builder()
                .name("new user")
                .email("new_user@example.com")
                .build();
    }

    @Test
    void getAll() {
        Mockito.when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        Collection<User> users = userService.getAll();

        Assertions.assertNotNull(users);
        Assertions.assertEquals(1, users.size());
        Assertions.assertEquals(user, users.iterator().next());

        Mockito.verify(userRepository, Mockito.times(1)).findAll();
    }

    @Test
    void getByIdSuccess() {
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        User result = userService.getById(user.getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(user, result);

        Mockito.verify(userRepository, Mockito.times(1)).findById(user.getId());
    }

    @Test
    void getByIdNotFound() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> userService.getById(1L));

        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    void createSuccess() {
        Mockito.when(userRepository.existsByEmail(Mockito.anyString())).thenReturn(false);
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);

        User result = userService.create(createUserRequestDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(user, result);

        Mockito.verify(userRepository, Mockito.times(1)).existsByEmail(Mockito.anyString());
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void createEmailAlreadyExists() {
        Mockito.when(userRepository.existsByEmail(Mockito.anyString())).thenReturn(true);

        Assertions.assertThrows(AlreadyExistException.class, () -> userService.create(createUserRequestDto));

        Mockito.verify(userRepository, Mockito.times(1)).existsByEmail(Mockito.anyString());
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void updateSuccess() {
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.existsByEmail(Mockito.anyString())).thenReturn(false);
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);

        User result = userService.update(user.getId(), updateUserRequestDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(user, result);

        Mockito.verify(userRepository, Mockito.times(1)).findById(user.getId());
        Mockito.verify(userRepository, Mockito.times(1)).existsByEmail(Mockito.anyString());
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void updateUserNotFound() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> userService.update(1L, updateUserRequestDto));

        Mockito.verify(userRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(userRepository, Mockito.never()).existsByEmail(Mockito.anyString());
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void updateEmailAlreadyExists() {
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.existsByEmail(Mockito.anyString())).thenReturn(true);

        Assertions.assertThrows(AlreadyExistException.class, () -> userService.update(user.getId(), updateUserRequestDto));

        Mockito.verify(userRepository, Mockito.times(1)).findById(user.getId());
        Mockito.verify(userRepository, Mockito.times(1)).existsByEmail(Mockito.anyString());
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void testDeleteUser() {
        Mockito.doNothing().when(userRepository).deleteById(1L);
        userService.delete(1L);
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(1L);
    }
}