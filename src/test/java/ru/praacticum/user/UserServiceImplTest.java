package ru.praacticum.user;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.practicum.config.AppConfig;
import ru.practicum.user.UserServiceImpl;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringJUnitConfig(
        {
                AppConfig.class,
                PersistenceConfig.class,
                UserServiceImpl.class
        }
)
public class UserServiceImplTest {
    public void testSaveUser() {

    }

    public void testGetAll() {

    }
}
