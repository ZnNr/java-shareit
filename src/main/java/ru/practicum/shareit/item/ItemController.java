package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public List<ItemDto> getAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен запрос GET /items");
        return itemService.getAll(userId);
    }

    @GetMapping("/{id}")
    public ItemDto getById(@PathVariable Long id) {
        log.info("Получен запрос GET /items/id");
        return itemService.get(id);
    }

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody ItemDto itemDto, Errors errors) {
        log.info("Получен запрос POST /items");
        if (errors.hasErrors()) {
            throw new ValidationException("Произошла ошибка валидации - " + errors.getAllErrors());
        } else {
            return itemService.add(itemDto, userId);
        }
    }

    @PatchMapping("/{id}")
    public ItemDto update(@RequestBody ItemDto itemDto, @PathVariable Long id,
                          @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен запрос PATCH /items/id");
        return itemService.update(itemDto, id, userId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        log.info("Получен запрос PATCH /items/search");
        return itemService.search(text);
    }


}
