package ru.practicum.user.repository;

import com.github.javafaker.Faker;
import org.springframework.stereotype.Repository;
import ru.practicum.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Repository
public class FakeUserRepository implements UserRepository {
    private final Faker faker = new Faker();
    private final List<User> users = new ArrayList<>();

    public FakeUserRepository() {
        fillUsers(5);
    }

    @Override
    public List<User> findAll() {
        return users;
    }

    @Override
    public User save(User user) {
        return null;
    }

    private User makeUser() {
        return User.builder()
                .id(faker.number().randomNumber())
                .email(faker.internet().emailAddress())
                .name(faker.name().fullName())
                .build();
    }

    private void fillUsers(int size) {
        for (int i = 0; i < size; i++) {
            users.add(makeUser());
        }
    }
}
