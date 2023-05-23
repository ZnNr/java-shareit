package ru.practicum.shareit.item.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.markers.Constants;
import ru.practicum.shareit.markers.Create;
import ru.practicum.shareit.markers.Update;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/items")
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    public ItemController(ItemClient itemClient) {
        this.itemClient = itemClient;
    }

    @GetMapping
    public ResponseEntity<Object> getByOwnerId(
            @RequestHeader(Constants.headerUserId) Long userId,
            @RequestParam(defaultValue = Constants.PAGE_DEFAULT_FROM) @PositiveOrZero Integer from,
            @RequestParam(defaultValue = Constants.PAGE_DEFAULT_SIZE) @Positive Integer size) {
        log.info("Получен запрос GET /items " + userId);
        return itemClient.getByOwnerId(userId, from, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@RequestHeader(Constants.headerUserId) Long userId,
                                          @PathVariable Long id) {
        log.info("Получен запрос GET /items/id  запрос на вещь с id" + id);
        return itemClient.getById(userId, id);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestHeader(Constants.headerUserId) Long userId,
                                      @Validated(Create.class) @RequestBody ItemDto itemDto) {
        log.info("Получен запрос POST /items " + itemDto);
        return itemClient.add(userId, itemDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@RequestHeader(Constants.headerUserId) Long userId,
                                         @PathVariable Long id,
                                         @Validated(Update.class) @RequestBody ItemDto itemDto) {
        log.info("Получен запрос PATCH /items/id " + "!Обновление вещи с id" + id + " на " + itemDto + " юзер с id" + userId);
        return itemClient.update(userId, id, itemDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        log.info("Получен запрос POST /items/id " + id);
        return itemClient.deleteItem(id);
    }


    @GetMapping("/search")
    public ResponseEntity<Object> search(
            @RequestParam String text,
            @RequestParam(defaultValue = Constants.PAGE_DEFAULT_FROM) @PositiveOrZero Integer from,
            @RequestParam(defaultValue = Constants.PAGE_DEFAULT_SIZE) @Positive Integer size) {
        log.info("Получен запрос PATCH /items/search " + text);
        return itemClient.search(text, from, size);
    }

    @PostMapping("{id}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(Constants.headerUserId) long userId,
                                             @PathVariable long id,
                                             @Valid @RequestBody CommentDto commentDto) {
        return itemClient.addComment(userId, id, commentDto);
    }
}