package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmailIsNotUniqueException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.ArrayList;
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
        if (!checkIsEmailUnique(user.getId(), user.getEmail())) {
            log.info("Ошибка добавления пользователя. Email занят.");
        }
        log.info("Пользователь с id = {} успешно создан", user);
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
        if (user.getEmail() != null && !user.getEmail().equals(updatedUser.getEmail()) && (checkIsEmailUnique(user.getId(), user.getEmail()))) {
            updatedUser.setEmail(user.getEmail());
        }
        log.info("Обновление пользователя с id = {} ", user.getId());
        return toUserDto(userStorage.update(updatedUser));
    }

    @Override
    public void delete(Long id) {
        userStorage.delete(id);
        log.info("Пользователь с id = {} успешно удален", id);
    }

    public boolean checkIsEmailUnique(Long id, String checkedEmail) {
        for (User user : userStorage.findAll()) {
            if (user.getEmail().equals(checkedEmail) && !(user.getId().equals(id))) {
                throw new EmailIsNotUniqueException("Email " + user.getEmail() + " уже занят");

            }
        }
        return true;
    }
}