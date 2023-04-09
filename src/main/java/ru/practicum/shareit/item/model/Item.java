package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * TODO Sprint add-controllers.
 */

@Data
@Builder
public class Item {
    //id — уникальный идентификатор вещи;
    private Long id;
    //owner — владелец вещи;
    private User owner;
    //name — краткое название;
    @NotNull
    @NotBlank(message = "Название не может быть пустым!")
    private String name;
    //description — развёрнутое описание;
    @NotBlank(message = "Описание не может быть пустым!")
    private String description;
    //available — статус о том, доступна или нет вещь для аренды;
    @NotNull(message = "Статус о том дуоступен ли товар или нет не должен быть пустым!")
    private Boolean available;
    //request — если вещь была создана по запросу другого пользователя, то в этом
//поле будет храниться ссылка на соответствующий запрос.
    private ItemRequest request;
}
