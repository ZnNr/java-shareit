package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserService {
    Collection<UserDto> getAllUsers();

    UserDto get(Long id);

    UserDto add(User user);

    UserDto update(User user, long id);

    void delete(Long id);
}
