package ru.yandex.practicum.filmorate.storage.implementation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
class MpaDbStorageTest {
    private final MpaStorage mpaStorage;

    @Autowired
    MpaDbStorageTest(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    @Test
    void getByIdTest() {
        Mpa mpa = mpaStorage.get(2L).get();
        assertEquals("PG", mpa.getName());
    }

    @Test
    void getAllGenresTest() {
        List<Mpa> mpaList = mpaStorage.getAllEntries();
        assertEquals(mpaList.get(0).getName(), "G");
        assertEquals(mpaList.get(1).getName(), "PG");
    }
}