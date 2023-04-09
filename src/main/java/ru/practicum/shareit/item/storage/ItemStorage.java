package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;

public interface ItemStorage {
    Collection<Item> getAll();

    Optional<Item> get(Long id);

    Item add(Item item);

    Item update(Item item);

    void delete(Long id);
}
