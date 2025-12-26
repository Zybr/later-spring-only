package ru.practicum.user.repository;

import ru.practicum.user.model.User;

import java.util.List;

public interface UserRepository {
    List<User> findAll();

    User save(User user);
}
