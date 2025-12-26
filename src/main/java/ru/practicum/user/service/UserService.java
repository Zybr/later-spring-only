package ru.practicum.user.service;

import ru.practicum.user.model.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    User saveUser(User user);
}
