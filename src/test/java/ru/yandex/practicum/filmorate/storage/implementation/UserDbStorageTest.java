package ru.yandex.practicum.filmorate.storage.implementation;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
class UserDbStorageTest {
    private final UserStorage userStorage;

    @Autowired
    UserDbStorageTest(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @AfterEach
    void clearStorageAfterEachTest() {
        userStorage.clear();
    }

    @Test
    void getAllEntriesTest() {
        User user = new User("mail@mail.ru", "login", "name",
                LocalDate.of(2001, 12, 2));
        userStorage.save(user);

        Optional<User> userOptional = userStorage.get(1L);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user1 ->
                        assertThat(user1).hasFieldOrPropertyWithValue("login", "login"));
    }

    @Test
    void getFilmByIdTest() {
        User user = new User("mail@mail.ru", "login", "name",
                LocalDate.of(2001, 12, 2));
        User user1 = new User("mail@mail.ru", "login1", "name1",
                LocalDate.of(2001, 12, 2));
        userStorage.save(user);
        userStorage.save(user1);
        List<User> referenceList = new ArrayList<>(Arrays.asList(user, user1));
        List<User> loadedUserList = userStorage.getAllEntries();
        assertEquals(referenceList, loadedUserList, "Возвращается некорректный список пользователей");
    }

    @Test
    void saveUserTest() {
        User user = new User("mail@mail.ru", "login", "name",
                LocalDate.of(2001, 12, 2));
        userStorage.save(user);

        Optional<User> userOptional = userStorage.get(1L);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user1 ->
                        assertThat(user1).hasFieldOrPropertyWithValue("name", "name")
                                .hasFieldOrPropertyWithValue("login", "login"));
    }

    @Test
    void updateUserTest() {
        User user = new User("mail@mail.ru", "login", "name",
                LocalDate.of(2001, 12, 2));
        userStorage.save(user);
        userStorage.update(new User("updated@mail.ru", "updated", "name",
                LocalDate.of(2001, 12, 2), 1L));
        Optional<User> userOptional = userStorage.get(1L);
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user1 ->
                        assertThat(user1).hasFieldOrPropertyWithValue("email", "updated@mail.ru")
                                .hasFieldOrPropertyWithValue("login", "updated"));
    }

    @Test
    void deleteUserTest() {
        User user = new User("mail@mail.ru", "login", "name",
                LocalDate.of(2001, 12, 2));
        userStorage.save(user);
        userStorage.save(new User("updated@mail.ru", "updated", "name",
                LocalDate.of(2001, 12, 2), 1L));
        userStorage.delete(2L);
        assertEquals(1, userStorage.getAllEntries().size(), "Удаление пользователей происходит некорректно");
    }
}