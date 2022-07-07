package ru.yandex.practicum.filmorate.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.LocalDateAdapter;

import java.time.LocalDate;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WebMvcTest(controllers = InMemoryController.class)
class UserControllerTest {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    @Autowired
    private MockMvc mockMvc;

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
        user.setId(3L);
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
        user.setId(5L);
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
        user.setId(4L);
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
                .andExpect(MockMvcResultMatchers.status().is5xxServerError()).andReturn();
        HttpStatus status = HttpStatus.valueOf(response.getResponse().getStatus());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, status, "При попытке обновить данные несуществующего " +
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
}