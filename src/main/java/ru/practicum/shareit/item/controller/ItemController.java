package ru.practicum.shareit.item.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemExtendedDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.markers.Create;
import ru.practicum.shareit.markers.Update;
import ru.practicum.shareit.user.conroller.UserController;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public List<ItemExtendedDto> getByOwnerId(@RequestHeader(UserController.headerUserId) Long userId) {
        log.info("Получен запрос GET /items " + userId);
        return itemService.getByOwnerId(userId);
    }

    @GetMapping("/{id}")
    public ItemExtendedDto getById(@RequestHeader(UserController.headerUserId) Long userId,
                                   @PathVariable Long id) {
        log.info("Получен запрос GET /items/id  запрос на вещь с id" + id);
        return itemService.getById(userId, id);
    }

    @PostMapping
    public ItemDto add(@RequestHeader(UserController.headerUserId) Long userId,
                       @Validated(Create.class) @RequestBody ItemDto itemDto) {
        log.info("Получен запрос POST /items " + itemDto);
        return itemService.add(userId, itemDto);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@RequestHeader(UserController.headerUserId) Long userId,
                          @PathVariable Long id,
                          @Validated(Update.class) @RequestBody ItemDto itemDto) {
        log.info("Получен запрос PATCH /items/id " + "!Обновление вещи с id" + id + " на " + itemDto + " юзер с id" + userId);
        return itemService.update(userId, id, itemDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        itemService.delete(id);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        log.info("Получен запрос PATCH /items/search " + text);
        return itemService.search(text);
    }

    @PostMapping("{id}/comment")
    public CommentDto addComment(@RequestHeader(UserController.headerUserId) long userId,
                                 @PathVariable long id,
                                 @Valid @RequestBody CommentRequestDto commentRequestDto) {
        return itemService.addComment(userId, id, commentRequestDto);
    }
}
