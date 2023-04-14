
package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    Collection<User> findAll();

    Optional<User> findById(Long id);

    User add(User user);

    User update(User user);

    boolean delete(Long id);
}