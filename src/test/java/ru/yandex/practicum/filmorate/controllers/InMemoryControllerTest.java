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
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.LocalDateAdapter;

import java.time.LocalDate;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WebMvcTest(controllers = InMemoryController.class)
class InMemoryControllerTest {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createNewUserBadEmailTest() throws Exception {
        User user = new User("mailmail.ru", "user", "name", LocalDate.of(2011, 11, 11));
        String json = gson.toJson(user);
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
        user.setId(3);
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
        user.setId(4);
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
        updatedUser.setId(1);
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
        user.setId(-1);
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
        user.setId(1);
        String jsonTest = "[" + gson.toJson(user) + "]";
        assertEquals(jsonTest, received, "Получение списка пользователей происходит некорректно");
    }

    @Test
    void createNewFilmWithBadName() throws Exception {
        Film film = new Film("", "description", LocalDate.of(2021, 12, 5), 100);
        String json = gson.toJson(film);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String message = Objects.requireNonNull(response.getResolvedException()).getMessage();
        assertTrue(message.contains("default message [name]"));
        assertTrue(message.contains("default message [must not be empty]"));
    }

    @Test
    void createNewFilmWithBadDescription() throws Exception {
        Film film = new Film("title", "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. " +
                "Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et " +
                "magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis,.", LocalDate.of(2021, 12, 5), 100);
        String json = gson.toJson(film);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String message = Objects.requireNonNull(response.getResolvedException()).getMessage();
        assertTrue(message.contains("default message [description]"));
        assertTrue(message.contains("default message [size must be between 0 and 200]"));
    }

    @Test
    void createNewUserWithBadReleaseDate() throws Exception {
        Film film = new Film("title", "description", LocalDate.of(1895, 12, 27), 100);
        String json = gson.toJson(film);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String message = Objects.requireNonNull(response.getResolvedException()).getMessage();
        assertTrue(message.contains("default message [releaseDate]"));
        assertTrue(message.contains("default message [Release date must be later than 28 Dec 1895]"));
    }

    @Test
    void createNewUserWithBadDurationValue() throws Exception {
        Film film = new Film("title", "description", LocalDate.of(2000, 12, 27), -100);
        String json = gson.toJson(film);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String message = Objects.requireNonNull(response.getResolvedException()).getMessage();
        assertTrue(message.contains("default message [duration]"));
        assertTrue(message.contains("default message [must be greater than 0]"));
    }

    @Test
    void updateFilmTest() throws Exception {
        Film film = new Film("title", "description", LocalDate.of(2000, 12, 27), 100);
        String json = gson.toJson(film);
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        Film filmUpdated = new Film("title", "descriptionUpdated", LocalDate.of(2000, 12, 27), 100);
        filmUpdated.setId(1);
        String jsonUpdated = gson.toJson(filmUpdated);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUpdated))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        String received = response.getResponse().getContentAsString();
        assertEquals(jsonUpdated, received, "Данные пользователя обновляются некорректно");
    }

    @Test
    void updateUnknownFilm() throws Exception {
        Film film = new Film("title", "description", LocalDate.of(2000, 12, 27), 100);
        film.setId(-1);
        String json = gson.toJson(film);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is5xxServerError()).andReturn();
        HttpStatus status = HttpStatus.valueOf(response.getResponse().getStatus());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, status, "При попытке обновить данные несуществующего " +
                " фильма выводится неверный код ответа сервера");
    }

    @Test
    void getAllFilms() throws Exception {
        Film film = new Film("title", "description", LocalDate.of(2000, 12, 27), 100);
        String json = gson.toJson(film);
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        String received = response.getResponse().getContentAsString();
        film.setId(1);
        String jsonTest = "[" + gson.toJson(film) + "]";
        assertEquals(jsonTest, received, "Получение списка фильмов происходит некорректно");
    }
}