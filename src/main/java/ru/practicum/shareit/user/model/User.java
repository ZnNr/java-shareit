package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    //id — уникальный идентификатор пользователя;
    private Long id;
    //name — имя или логин пользователя;
    private String name;
    //email — адрес электронной почты пользователя
    @NotBlank(message = "Email обязателен")
    @Email(message = "Неверный email")
    private String email;
}