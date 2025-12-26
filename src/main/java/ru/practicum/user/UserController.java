package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    @GetMapping()
    public List<User> getAllUsers() {
        return service.getAllUsers();
    }

    @PostMapping()
    public User saveUser(
            @RequestBody User user
    ) {
        return service.saveUser(user);
    }
}
