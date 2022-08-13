package ru.yandex.practicum.filmorate.storage.implementation;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
class FriendsDbStorageTest {
    private final UserStorage userStorage;
    private final FriendsStorage friendsStorage;

    @Autowired
    FriendsDbStorageTest(UserStorage userStorage, FriendsStorage friendsStorage) {
        this.userStorage = userStorage;
        this.friendsStorage = friendsStorage;
    }

    @AfterEach
    void clearStorageAfterEachTest() {
        userStorage.clear();
    }

    @Test
    void userAddFriend() {
        User user = new User("mail@mail.ru", "user", "name", LocalDate.of(2011, 11, 11));
        User user1 = new User("mail@mail.ru", "user1", "name1", LocalDate.of(2012, 11, 11));
        userStorage.save(user);
        userStorage.save(user1);
        friendsStorage.addFriends(new Friendship(1L, 2L));
        List<Long> userFriendList = friendsStorage.getFriendList(1L);
        assertEquals(user1.getId(), userFriendList.get(0), "Добавление в друзья проходит некорректно");
    }

    @Test
    void userGetFriends() {
        User user = new User("mail@mail.ru", "user", "name", LocalDate.of(2011, 11, 11));
        User user1 = new User("mail@mail.ru", "user1", "name1", LocalDate.of(2012, 11, 11));
        userStorage.save(user);
        userStorage.save(user1);
        Friendship friendship = new Friendship(1L, 2L);
        friendsStorage.addFriends(friendship);
        List<Long> userFriendList = friendsStorage.getFriendList(1L);
        assertEquals(userFriendList.size(), 1, "Возвращается некорректный список друзей для пользователя");
    }

    @Test
    void friendRemoveTest() {
        User user = new User("mail@mail.ru", "user", "name", LocalDate.of(2011, 11, 11));
        User user1 = new User("mail@mail.ru", "user1", "name1", LocalDate.of(2012, 11, 11));
        Friendship friendship = new Friendship(1L, 2L);
        userStorage.save(user);
        userStorage.save(user1);
        friendsStorage.addFriends(friendship);
        friendsStorage.deleteFriend(friendship);
        assertTrue(friendsStorage.getFriendList(1L).isEmpty(), "Удаление из списка друзей проходит некорректно");
        assertTrue(friendsStorage.getFriendList(2L).isEmpty(), "Удаление из списка друзей проходит некорректно");
    }

    @Test
    void getCommonFriendsTest() {
        User user = new User("mail@mail.ru", "user", "name", LocalDate.of(2011, 11, 11));
        User user1 = new User("mail@mail.ru", "user1", "name1", LocalDate.of(2012, 11, 11));
        User user2 = new User("mail@mail.ru", "user2", "name2", LocalDate.of(2012, 11, 11));
        userStorage.save(user);
        userStorage.save(user1);
        userStorage.save(user2);
        Friendship friendship = new Friendship(1L, 2L);
        Friendship friendship1 = new Friendship(1L, 3L);
        Friendship friendship2 = new Friendship(2L, 3L);
        friendsStorage.addFriends(friendship);
        friendsStorage.addFriends(friendship1);
        friendsStorage.addFriends(friendship2);
        assertEquals(2L, friendsStorage.getCommonFriends(friendship1).get(0), "Возвращение" +
                " списка общих друзей проходит некорректно");
    }
}