package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.markers.Constants;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Validated
@Controller
@RequestMapping(path = "/items")
@Slf4j
@RequiredArgsConstructor
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader(name = Constants.headerUserId) Long ownerId,
                                          @RequestBody @Valid ItemDto itemDto) {
        log.info("Получен запрос POST /items создание вещи item={}, userid={}", itemDto, ownerId);
        return itemClient.addItem(ownerId, itemDto);
    }

    @PostMapping(path = "/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(name = Constants.headerUserId) Long authorId,
                                             @PathVariable Long itemId,
                                             @RequestBody @Valid CommentDto commentDto) {
        return itemClient.addComment(authorId, itemId, commentDto);
    }

    @GetMapping
    public ResponseEntity<Object> getOwnerItems(
            @RequestHeader(name = Constants.headerUserId) Long ownerId,
            @RequestParam(defaultValue = Constants.PAGE_DEFAULT_FROM) @PositiveOrZero Integer from,
            @RequestParam(defaultValue = Constants.PAGE_DEFAULT_SIZE) @Positive Integer size) {
        log.info("Получен запрос GET owner items, ownerId={}, from={}, size={}", ownerId, from, size);
        return itemClient.getOwnerItems(ownerId, from, size);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Object> getItem(@RequestHeader(name = Constants.headerUserId) Long requestorId,
                                          @PathVariable Long id) {
        log.info("Получен запрос GET item, requesterId={}, itemId={}", requestorId, id);
        return itemClient.getItem(requestorId, id);
    }

    @PatchMapping(path = "/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(name = Constants.headerUserId) Long ownerId,
                                             @PathVariable Long itemId,
                                             @RequestBody ItemDto itemDto) {
        log.info("Получен запрос PATCH  " + "!Обновление вещи с id" + itemId + " на " + itemDto + " юзер с id" + ownerId);
        return itemClient.updateItem(ownerId, itemId, itemDto);
    }

    @GetMapping(path = "/search")
    public ResponseEntity<Object> getItemBySearch(
            @RequestHeader(name = Constants.headerUserId) Long ownerId,
            @RequestParam String text,
            @RequestParam(defaultValue = Constants.PAGE_DEFAULT_FROM) @PositiveOrZero Integer from,
            @RequestParam(defaultValue = Constants.PAGE_DEFAULT_SIZE) @Positive Integer size) {
        log.info("Получен запрос PATCH  Поиск вещей ownerId={}, text={}, from={}, size={}", ownerId, text, from, size);
        return itemClient.searchAvailableItems(ownerId, text, from, size);
    }
}