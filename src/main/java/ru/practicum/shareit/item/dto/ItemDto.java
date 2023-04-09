package ru.practicum.shareit.item.dto;

import lombok.Builder;
import ru.practicum.shareit.request.ItemRequest;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
public class ItemDto {

    private Long id;

    @NotBlank(message = "Название не может быть пустым!")
    private String name;

    @NotBlank(message = "Описание не может быть пустым!")
    private String description;

    @NotNull(message = "Статус о том дуоступен ли товар или нет не должен быть пустым!")
    private Boolean available;

    private ItemRequest request;
}