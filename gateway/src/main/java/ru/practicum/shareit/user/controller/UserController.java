package ru.practicum.shareit.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.markers.Create;
import ru.practicum.shareit.markers.Update;
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.user.dto.UserDto;

@RestController
@RequestMapping(path = "/users")
@Slf4j
public class UserController {

    private final UserClient userClient;

    public UserController(UserClient userClient) {
        this.userClient = userClient;
    }


    @GetMapping
    public ResponseEntity<Object> getAll() {
        log.info("Получен запрос GET /users");
        return userClient.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(
            @PathVariable Long id) {
        log.info("Получен запрос GET /users/id " + id);
        return userClient.getById(id);
    }

    @PostMapping
    public ResponseEntity<Object> add(
            @Validated(Create.class)
            @RequestBody UserDto userDto) {
        log.info("Получен запрос POST /users " + userDto);
        return userClient.add(userDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id,
                                         @Validated(Update.class)
                                         @RequestBody UserDto userDto) {
        log.info("Получен запрос PATCH /users/id " + "!Обновление пользователя с id " + id + " на " + userDto);
        return userClient.update(userDto, id);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id) {
        log.info("Получен запрос POST /users/id " + id);
        userClient.deleteUser(id);
    }
}