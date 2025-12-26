package ru.practicum.user;

import com.github.javafaker.Faker;
import org.springframework.stereotype.Repository;

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
        return new User(
                faker.number().randomNumber(),
                faker.internet().emailAddress(),
                faker.name().fullName()
        );
    }

    private void fillUsers(int size) {
        for (int i = 0; i < size; i++) {
            users.add(makeUser());
        }
    }
}
