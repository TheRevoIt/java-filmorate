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
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.LocalDateAdapter;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Import(FilmController.class)
@SpringBootTest
@AutoConfigureMockMvc
class FilmControllerTest {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    void clearMapAfterTests() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/films/clear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        mockMvc.perform(MockMvcRequestBuilders.get("/users/clear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
    }

    @Test
    void createNewFilmWithBadName() throws Exception {
        Film film = new Film("", "description", LocalDate.of(2021, 12, 5),
                100);
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
                "magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis,.",
                LocalDate.of(2021, 12, 5), 100);
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
    void createNewFilmWithNullName() throws Exception {
        Film film = new Film(null, "description", LocalDate.of(2021, 12, 5), 100);
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
    void createNewFilmWithNullDescription() throws Exception {
        Film film = new Film("name", null, LocalDate.of(2021, 12, 5), 100);
        String json = gson.toJson(film);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String message = Objects.requireNonNull(response.getResolvedException()).getMessage();
        assertTrue(message.contains("default message [description]"));
        assertTrue(message.contains("default message [must not be null]"));
    }

    @Test
    void createNewFilmWithNullReleaseDate() throws Exception {
        Film film = new Film("name", "description", null, 100);
        String json = gson.toJson(film);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String message = Objects.requireNonNull(response.getResolvedException()).getMessage();
        assertTrue(message.contains("default message [releaseDate]"));
        assertTrue(message.contains("default message [must not be null]"));
    }

    @Test
    void createNewFilmWithNullDuration() throws Exception {
        Film film = new Film("name", "description", LocalDate.of(2021, 12, 5),
                null);
        String json = gson.toJson(film);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String message = Objects.requireNonNull(response.getResolvedException()).getMessage();
        assertTrue(message.contains("default message [duration]"));
        assertTrue(message.contains("default message [must not be null]"));
    }

    @Test
    void updateFilmTest() throws Exception {
        Film film = new Film("title", "description", LocalDate.of(2000, 12, 27),
                100);
        String json = gson.toJson(film);
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        Film filmUpdated = new Film("title", "descriptionUpdated", LocalDate.of(2000, 12,
                27), 100);
        filmUpdated.setId(1L);
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
        Film film = new Film("title", "description", LocalDate.of(2000, 12, 27),
                100);
        film.setId(-1L);
        String json = gson.toJson(film);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        HttpStatus status = HttpStatus.valueOf(response.getResponse().getStatus());
        assertEquals(HttpStatus.NOT_FOUND, status, "При попытке обновить данные несуществующего " +
                " фильма выводится неверный код ответа сервера");
    }

    @Test
    void getAllFilms() throws Exception {
        Film film = new Film("title", "description", LocalDate.of(2000, 12, 27),
                100);
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
        film.setId(1L);
        String jsonTest = "[" + gson.toJson(film) + "]";
        assertEquals(jsonTest, received, "Получение списка фильмов происходит некорректно");
    }

    @Test
    void getFilmById() throws Exception {
        Film film = new Film("title", "description", LocalDate.of(2000, 12, 27),
                100);
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(film)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/films/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        String received = response.getResponse().getContentAsString();
        String expected = "{\"name\":\"title\",\"description\":\"description\",\"releaseDate\":\"2000-12-27\"," +
                "\"duration\":100,\"id\":1,\"likesIds\":[]}";
        assertEquals(expected, received, "Получение фильма по id происходит некорректно");
    }

    @Test
    void getUnknownFilmById() throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/films/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String received = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
        String expected = "Фильм с id=1 не найден";
        assertEquals(expected, received, "Получение фильма по id происходит некорректно");
    }

    @Test
    void filmLikeByUserTest() throws Exception {
        User user = new User("mail@mail.ru", "user", "name", LocalDate.of(2011, 11, 11));
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        Film film = new Film("title", "description", LocalDate.of(2000, 12, 27),
                100);
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(film)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        mockMvc.perform(MockMvcRequestBuilders.put("/films/1/like/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/films/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(film)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        String received = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
        String expected = "{\"name\":\"title\",\"description\":\"description\",\"releaseDate\":\"2000-12-27\"," +
                "\"duration\":100,\"id\":1,\"likesIds\":[1]}";
        assertEquals(expected, received, "Некорректно обрабатывается лайк фильма пользователем");
    }

    @Test
    void filmLikeByUnknownUserTest() throws Exception {
        Film film = new Film("title", "description", LocalDate.of(2000, 12, 27),
                100);
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(film)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.put("/films/1/like/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String received = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
        String expected = "Пользователь с id=1 не найден";
        assertEquals(expected, received, "Некорректно обрабатывается лайк фильма пользователем");
    }

    @Test
    void unknownFilmLikeByUserTest() throws Exception {
        User user = new User("mail@mail.ru", "user", "name", LocalDate.of(2011, 11, 11));
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.put("/films/1/like/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String received = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
        String expected = "Фильм с id=1 не найден";
        assertEquals(expected, received, "Некорректно обрабатывается лайк фильма пользователем");
    }

    @Test
    void removeLikeTest() throws Exception {
        User user = new User("mail@mail.ru", "user", "name", LocalDate.of(2011, 11, 11));
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        Film film = new Film("title", "description", LocalDate.of(2000, 12, 27),
                100);
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(film)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        mockMvc.perform(MockMvcRequestBuilders.put("/films/1/like/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        mockMvc.perform(MockMvcRequestBuilders.delete("/films/1/like/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/films/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(film)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        String received = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
        String expected = "{\"name\":\"title\",\"description\":\"description\",\"releaseDate\":\"2000-12-27\"," +
                "\"duration\":100,\"id\":1,\"likesIds\":[]}";
        assertEquals(expected, received, "Некорректно обрабатывается удаление лайка");
    }

    @Test
    void removeLikeUnknownUser() throws Exception {
        User user = new User("mail@mail.ru", "user", "name", LocalDate.of(2011, 11, 11));
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        Film film = new Film("title", "description", LocalDate.of(2000, 12, 27),
                100);
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(film)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        mockMvc.perform(MockMvcRequestBuilders.put("/films/1/like/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.delete("/films/1/like/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String received = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
        String expected = "Пользователь с id=2 не найден";
        assertEquals(expected, received, "Некорректно обрабатывается удаление лайка");
    }


    @Test
    void getFilmsSortedByLikesNoCountParameter() throws Exception {
        User user = new User("mail@mail.ru", "user", "name", LocalDate.of(2011, 11, 11));
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        User user1 = new User("mail@mail.ru", "user1", "name", LocalDate.of(2011, 11, 11));
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user1)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        User user2 = new User("mail@mail.ru", "user2", "name", LocalDate.of(2011, 11, 11));
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user2)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        Film film = new Film("title", "description", LocalDate.of(2000, 12, 27),
                100);
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(film)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        Film film1 = new Film("title1", "description", LocalDate.of(2000, 12, 27),
                100);
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(film1)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        Film film2 = new Film("title2", "description", LocalDate.of(2000, 12, 27),
                100);
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(film2)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        mockMvc.perform(MockMvcRequestBuilders.put("/films/1/like/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        mockMvc.perform(MockMvcRequestBuilders.put("/films/2/like/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        mockMvc.perform(MockMvcRequestBuilders.put("/films/2/like/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        mockMvc.perform(MockMvcRequestBuilders.put("/films/2/like/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        mockMvc.perform(MockMvcRequestBuilders.put("/films/3/like/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        mockMvc.perform(MockMvcRequestBuilders.put("/films/3/like/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/films/popular")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        String received = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
        String expected = "[{\"name\":\"title1\",\"description\":\"description\",\"releaseDate\":\"2000-12-27\"," +
                "\"duration\":100,\"id\":2,\"likesIds\":[1,2,3]}," +
                "{\"name\":\"title2\",\"description\":\"description\",\"releaseDate\":\"2000-12-27\"," +
                "\"duration\":100,\"id\":3,\"likesIds\":[1,3]}," +
                "{\"name\":\"title\",\"description\":\"description\",\"releaseDate\":\"2000-12-27\"," +
                "\"duration\":100,\"id\":1,\"likesIds\":[2]}]";

        assertEquals(expected, received, "Некорректно обрабатывается удаление лайка");
    }

    @Test
    void getFilmsSortedByLikesNegativeCountParameter() throws Exception {
        User user = new User("mail@mail.ru", "user", "name", LocalDate.of(2011, 11, 11));
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        User user1 = new User("mail@mail.ru", "user1", "name", LocalDate.of(2011, 11, 11));
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user1)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        User user2 = new User("mail@mail.ru", "user2", "name", LocalDate.of(2011, 11, 11));
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user2)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        Film film = new Film("title", "description", LocalDate.of(2000, 12, 27),
                100);
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(film)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        Film film1 = new Film("title1", "description", LocalDate.of(2000, 12, 27),
                100);
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(film1)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        Film film2 = new Film("title2", "description", LocalDate.of(2000, 12, 27),
                100);
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(film2)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        mockMvc.perform(MockMvcRequestBuilders.put("/films/1/like/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        mockMvc.perform(MockMvcRequestBuilders.put("/films/2/like/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        mockMvc.perform(MockMvcRequestBuilders.put("/films/2/like/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        mockMvc.perform(MockMvcRequestBuilders.put("/films/2/like/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        mockMvc.perform(MockMvcRequestBuilders.put("/films/3/like/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        mockMvc.perform(MockMvcRequestBuilders.put("/films/3/like/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/films/popular?count=-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String received = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
        String expected = "getFilmsSortedByLikes.count: must be greater than 0";
        assertEquals(expected, received, "Неправильно обрабатываются некорректные значения count");
    }

    @Test
    void getFilmsSortedByLikesCount() throws Exception {
        User user = new User("mail@mail.ru", "user", "name", LocalDate.of(2011, 11, 11));
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        User user1 = new User("mail@mail.ru", "user1", "name", LocalDate.of(2011, 11, 11));
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user1)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        User user2 = new User("mail@mail.ru", "user2", "name", LocalDate.of(2011, 11, 11));
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user2)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        Film film = new Film("title", "description", LocalDate.of(2000, 12, 27),
                100);
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(film)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        Film film1 = new Film("title1", "description", LocalDate.of(2000, 12, 27),
                100);
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(film1)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        Film film2 = new Film("title2", "description", LocalDate.of(2000, 12, 27),
                100);
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(film2)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        mockMvc.perform(MockMvcRequestBuilders.put("/films/1/like/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        mockMvc.perform(MockMvcRequestBuilders.put("/films/2/like/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        mockMvc.perform(MockMvcRequestBuilders.put("/films/2/like/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        mockMvc.perform(MockMvcRequestBuilders.put("/films/2/like/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        mockMvc.perform(MockMvcRequestBuilders.put("/films/3/like/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        mockMvc.perform(MockMvcRequestBuilders.put("/films/3/like/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/films/popular?count=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        String received = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
        String expected = "[{\"name\":\"title1\",\"description\":\"description\",\"releaseDate\":\"2000-12-27\"," +
                "\"duration\":100,\"id\":2,\"likesIds\":[1,2,3]}]";
        assertEquals(expected, received, "Некорректно обрабатывается удаление лайка");
    }
}