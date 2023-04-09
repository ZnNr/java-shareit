package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmailIsNotUniqueException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static ru.practicum.shareit.user.UserMapper.*;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private static final String USER_NOT_EXISTS_MSG = "Пользователь с id = %d не существует";
    private final UserStorage userStorage;

    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }


    @Override
    public List<UserDto> getAllUsers() {
        log.info("Список всех пользователей успешно отправлен");
        List<UserDto> users = new ArrayList<>();
        for (User user : userStorage.findAll()) {
            users.add(toUserDto(user));
        }
        return users;
    }

    @Override
    public UserDto get(Long id) {
        User user = userStorage.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(String.format(USER_NOT_EXISTS_MSG, id)));
        log.info("Пользователь с id = {} успешно отправлен", id);
        return toUserDto(user);
    }

    @Override
    public UserDto add(User user) {
        checkIsEmailUnique(user);
        return toUserDto(userStorage.add(user));
    }

    @Override
    public UserDto update(User user, long id) {
        user.setId(id);
        User updatedUser = userStorage.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_EXISTS_MSG, user.getId())));
        if (user.getName() != null) {
            updatedUser.setName(user.getName());
        }
        if (user.getEmail() != null && !user.getEmail().equals(updatedUser.getEmail())) {
            checkIsEmailUnique(user);
            updatedUser.setEmail(user.getEmail());
        }
        log.info("Пользователь с id = {} успешно обновлен", user.getId());
        return toUserDto(userStorage.update(updatedUser));
    }

    @Override
    public void delete(Long id) {
        get(id);
        log.info("Пользователь с id = {} успешно удален", id);
        userStorage.delete(id);
    }

    private void checkIsEmailUnique(User user) {
        for (User u : userStorage.findAll()) {
            if (user.getEmail().equals(u.getEmail())) {
                throw new EmailIsNotUniqueException(String.format("Пользователь с таким email {} уже зарегистрирован", user.getEmail()));
            }
        }
    }
}
