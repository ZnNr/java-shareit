package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class UserStorageImpl implements UserStorage {
    private final Map<Long, User> users;
    private Long id = 0L;

    public UserStorageImpl() {
        this.users = new HashMap<>();
    }


    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public Optional<User> findById(Long id) {
        User user = users.get(id);
        return user == null ?
                Optional.empty() :
                Optional.of(user);
    }

    @Override
    public User add(User user) {
        ++id;
        user.setId(id);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public boolean delete(Long id) {
        return users.remove(id) != null;
    }
}
