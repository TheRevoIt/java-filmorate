package ru.yandex.practicum.filmorate.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.LocalDateAdapter;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Import(UserController.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    void clearMapAfterTests() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/clear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
    }

    @Test
    void createNewUserBadEmailTest() throws Exception {
        User user = new User("mailmail.ru", "user", "name", LocalDate.of(2011, 11, 11));
        String json = gson.toJson(user);
        System.out.println(json);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String message = Objects.requireNonNull(response.getResolvedException()).getMessage();
        assertTrue(message.contains("default message [email]"));
        assertTrue(message.contains("default message [must be a well-formed email address]"));
    }

    @Test
    void createNewUserFailLoginTest() throws Exception {
        User user = new User("mail@mail.ru", "user user", "name", LocalDate.of(2011, 11, 11));
        String json = gson.toJson(user);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String message = Objects.requireNonNull(response.getResolvedException()).getMessage();
        assertTrue(message.contains("default message [login]"));
        assertTrue(message.contains("default message [field shouldn't contain whitespaces]"));
    }

    @Test
    void createNewUserFailBirthdayTest() throws Exception {
        User user = new User("mail@mail.ru", "user", "name", LocalDate.of(2023, 11, 11));
        String json = gson.toJson(user);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String message = Objects.requireNonNull(response.getResolvedException()).getMessage();
        assertTrue(message.contains("default message [birthday]"));
        assertTrue(message.contains("default message [must be a date in the past or in the present]"));
    }

    @Test
    void createNewUserWithEmptyNameTest() throws Exception {
        User user = new User("mail@mail.ru", "user", "", LocalDate.of(2003, 11, 11));
        String json = gson.toJson(user);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        String received = response.getResponse().getContentAsString();
        user.setId(1L);
        user.setName("user");
        String jsonTest = gson.toJson(user);
        assertEquals(jsonTest, received, "Некорректно обрабатывается случай создания пользователя с пустым именем");
    }

    @Test
    void createNewUserNullEmailTest() throws Exception {
        User user = new User(null, "user", "name", LocalDate.of(2011, 11, 11));
        String json = gson.toJson(user);
        System.out.println(json);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String message = Objects.requireNonNull(response.getResolvedException()).getMessage();
        assertTrue(message.contains("default message [email]"));
        assertTrue(message.contains("default message [must not be null]"));
    }

    @Test
    void createNewUserNullLoginTest() throws Exception {
        User user = new User("mail@mail.ru", null, "name", LocalDate.of(2011, 11, 11));
        String json = gson.toJson(user);
        System.out.println(json);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String message = Objects.requireNonNull(response.getResolvedException()).getMessage();
        assertTrue(message.contains("default message [login]"));
        assertTrue(message.contains("default message [must not be empty]"));
    }

    @Test
    void createNewUserNullDateTest() throws Exception {
        User user = new User("mail@mail.ru", "user", "name", null);
        String json = gson.toJson(user);
        System.out.println(json);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String message = Objects.requireNonNull(response.getResolvedException()).getMessage();
        assertTrue(message.contains("default message [birthday]"));
        assertTrue(message.contains("default message [must not be null]"));
    }

    @Test
    void createNewUserNullNameTest() throws Exception {
        User user = new User("mail@mail.ru", "user", null, LocalDate.of(2011, 11, 11));
        String json = gson.toJson(user);
        System.out.println(json);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        String received = response.getResponse().getContentAsString();
        user.setId(1L);
        user.setName("user");
        String jsonTest = gson.toJson(user);
        assertEquals(jsonTest, received, "Некорректно обрабатывается случай создания пользователя с пустым именем");
    }

    @Test
    void createNewUserTest() throws Exception {
        User user = new User("mail@mail.ru", "user", "name", LocalDate.of(2011, 11, 11));
        String json = gson.toJson(user);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        String received = response.getResponse().getContentAsString();
        user.setId(1L);
        String jsonTest = gson.toJson(user);
        assertEquals(jsonTest, received, "Добавление пользователя происходит некорректно");
    }

    @Test
    void updateUserTest() throws Exception {
        User user = new User("mail@mail.ru", "user", "name", LocalDate.of(2011, 11, 11));
        String json = gson.toJson(user);
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        User updatedUser = new User("mail@mail.ru", "userUpdated", "nameUpdated", LocalDate.of(2011, 11, 11));
        updatedUser.setId(1L);
        String jsonUpdated = gson.toJson(updatedUser);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUpdated))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        String received = response.getResponse().getContentAsString();
        assertEquals(jsonUpdated, received, "Данные пользователя обновляются некорректно");
    }

    @Test
    void updateUnknownUserTest() throws Exception {
        User user = new User("mail@mail.ru", "user", "name", LocalDate.of(2013, 11, 11));
        user.setId(-1L);
        String json = gson.toJson(user);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        HttpStatus status = HttpStatus.valueOf(response.getResponse().getStatus());
        assertEquals(HttpStatus.NOT_FOUND, status, "При попытке обновить данные несуществующего " +
                " пользователя выводится неверный код ответа сервера");
    }

    @Test
    void getAllUsersTest() throws Exception {
        User user = new User("mail@mail.ru", "user", "name", LocalDate.of(2011, 11, 11));
        String json = gson.toJson(user);
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        String received = response.getResponse().getContentAsString();
        user.setId(1L);
        String jsonTest = "[" + gson.toJson(user) + "]";
        assertEquals(jsonTest, received, "Получение списка пользователей происходит некорректно");
    }

    @Test
    void getUserById() throws Exception {
        User user = new User("mail@mail.ru", "user", "name", LocalDate.of(2011, 11, 11));
        String json = gson.toJson(user);
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        String received = response.getResponse().getContentAsString();
        String expected = "{\"id\":1,\"email\":\"mail@mail.ru\",\"login\":\"user\",\"name\":\"name\",\"birthday\":\"" +
                "2011-11-11\",\"friendIds\":[]}";
        assertEquals(expected, received, "Получение пользователя по id происходит некорректно");
    }

    @Test
    void getUnknownUserById() throws Exception {
        User user = new User("mail@mail.ru", "user", "name", LocalDate.of(2011, 11, 11));
        String json = gson.toJson(user);
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/users/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String received = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
        String expected = "Пользователь с id=5 не найден";
        assertEquals(expected, received, "Получение пользователя по id происходит некорректно");
    }

    @Test
    void getUserCommonFriendEmpty() throws Exception {
        User user = new User("mail@mail.ru", "user", "name", LocalDate.of(2011, 11, 11));
        User user1 = new User("mail@mail.ru", "user1", "name1", LocalDate.of(2012, 11, 11));
        String json = gson.toJson(user);
        String json1 = gson.toJson(user1);
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json1))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/users/1/friends/common/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        String received = response.getResponse().getContentAsString();
        assertEquals("[]", received, "Получение  списка общих друзей происходит некорректно");
    }

    @Test
    void userAddFriend() throws Exception {
        User user = new User("mail@mail.ru", "user", "name", LocalDate.of(2011, 11, 11));
        User user1 = new User("mail@mail.ru", "user1", "name1", LocalDate.of(2012, 11, 11));
        String json = gson.toJson(user);
        String json1 = gson.toJson(user1);
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json1))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        mockMvc.perform(MockMvcRequestBuilders.put("/users/1/friends/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        String expected = "{\"id\":1,\"email\":\"mail@mail.ru\",\"login\":\"user\",\"name\":\"name\",\"birthday\":\"" +
                "2011-11-11\",\"friendIds\":[2]}";
        assertEquals(expected, response.getResponse().getContentAsString(), "Добавление в друзья проходит некорректно");
    }


    @Test
    void userAddFriendUnknown() throws Exception {
        User user = new User("mail@mail.ru", "user", "name", LocalDate.of(2011, 11, 11));
        String json = gson.toJson(user);
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.put("/users/1/friends/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String expected = "Пользователь с id=2 не найден";
        assertEquals(expected, response.getResponse().getContentAsString(StandardCharsets.UTF_8), "Добавление в друзья проходит некорректно");
    }

    @Test
    void getCommonFriendsTest() throws Exception {
        User user = new User("mail@mail.ru", "user", "name", LocalDate.of(2011, 11, 11));
        User user1 = new User("mail@mail.ru", "user1", "name1", LocalDate.of(2012, 11, 11));
        User user2 = new User("mail@mail.ru", "user2", "name2", LocalDate.of(2012, 11, 11));
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user1)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user2)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        mockMvc.perform(MockMvcRequestBuilders.put("/users/1/friends/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        mockMvc.perform(MockMvcRequestBuilders.put("/users/1/friends/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/users/2/friends/common/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        String expected = "[{\"id\":1,\"email\":\"mail@mail.ru\",\"login\":\"user\",\"name\":\"name\",\"birthday\":\"" +
                "2011-11-11\",\"friendIds\":[2,3]}]";
        assertEquals(expected, response.getResponse().getContentAsString(), "Возвращение списка общих друзей проходит некорректно");
    }

    @Test
    void removeFriendTest() throws Exception {
        User user = new User("mail@mail.ru", "user", "name", LocalDate.of(2011, 11, 11));
        User user1 = new User("mail@mail.ru", "user1", "name1", LocalDate.of(2012, 11, 11));
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user1)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        mockMvc.perform(MockMvcRequestBuilders.put("/users/1/friends/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.delete("/users/1/friends/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        String expected = "{\"id\":1,\"email\":\"mail@mail.ru\",\"login\":\"user\",\"name\":\"name\",\"birthday\":\"" +
                "2011-11-11\",\"friendIds\":[]}";
        assertEquals(expected, response.getResponse().getContentAsString(), "Удаление из списка друзей проходит некорректно");
    }
}