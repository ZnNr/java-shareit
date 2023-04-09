package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class ItemStorageImpl implements ItemStorage {
    private final Map<Long, Item> items;
    private Long id = 0L;

    public ItemStorageImpl() {
        this.items = new HashMap<>();
    }

    @Override
    public Collection<Item> getAll() {
        return items.values();
    }

    @Override
    public Optional<Item> get(Long id) {
        Item item = items.get(id);
        return item == null ?
                Optional.empty() :
                Optional.of(item);
    }

    @Override
    public Item add(Item item) {
        ++id;
        item.setId(id);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(Item item) {
        return items.put(item.getId(), item);
    }

    @Override
    public void delete(Long id) {
        items.remove(id);
    }
}
