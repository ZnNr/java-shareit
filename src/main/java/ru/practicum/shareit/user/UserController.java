package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<UserDto> getAll() {
        log.info("Получен запрос GET /users");
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        log.info("Получен запрос GET /users/" + id);
        return userService.get(id);
    }

    @PostMapping
    public UserDto add(@Validated @RequestBody User user, Errors errors) {
        log.info("Получен запрос POST /users/");
        if (errors.hasErrors()) {
            throw new ValidationException("Произошла ошибка валидации - " + errors.getAllErrors());
        } else {
            return userService.add(user);
        }
    }

    @PatchMapping("/{id}")
    public UserDto update(@RequestBody User newUser, @PathVariable long id, Errors errors) {
        log.info("Получен запрос PATCH /users/id");
        if (errors.hasErrors()) {
            throw new ValidationException("Произошла ошибка валидации - " + errors.getAllErrors());
        } else {
            return userService.update(newUser, id);
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        log.info("Получен запрос DELETE /users/{}", id);
        userService.delete(id);
    }
}