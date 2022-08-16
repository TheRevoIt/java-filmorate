# Описание проекта java-filmorate

Database scheme:
https://app.quickdatabasediagrams.com/#/d/2W3D7u

![QuickDBD-Filmorate Database](https://user-images.githubusercontent.com/70698014/184476558-c7ae2061-0935-4979-b158-2521987a368f.png)

### Примеры запросов для приложения

#### FILMS

1. GET /films Получение списка всех фильмов
   ````
   SELECT * FROM FILMS JOIN MPA on FILMS.MPA_ID = MPA.MPA_ID 
   ````
2. GET /films/{id} Получение фильма по id
   ````
   SELECT FILM_ID, TITLE, DESCRIPTION, RELEASE_DATE, DURATION, MPA.MPA_ID, NAME FROM FILMS JOIN MPA" +
               " on FILMS.MPA_ID = MPA.MPA_ID WHERE FILM_ID = ?
   ````
3. POST /films Добавление нового фильма
   ````
   insert into films (title, description, release_date, duration, mpa_id) values (?, ?, ?, ?, ?)
   ````
4. PUT /films Обновление данных существующего фильма
   ````
   UPDATE FILMS SET TITLE = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, MPA_ID = ? WHERE FILM_ID = ?
   ````
5. DELETE /films/{filmId} Удаление фильма по id
   ````
   DELETE FROM FILMS WHERE FILM_ID=?
   ````
6. PUT /films/{id}/like/{userId} Лайк фильма пользователем
   ````
   INSERT INTO LIKES (FILM_ID, USER_ID) VALUES (?, ?)
   ````
7. DELETE /films/{id}/like/{userId} Удаления лайка с фильма пользователем
   ````
   DELETE FROM LIKES WHERE (FILM_ID = ? AND USER_ID = ? )
   ````
8. GET films/popular?count={n} Получение списка из n фильмов, отсортированного по количеству лайков
   ````
   SELECT F.FILM_ID from FILMS AS F left join (SELECT * FROM LIKES group by USER_ID, FILM_ID) AS M" +
                " ON F.FILM_ID=M.FILM_ID GROUP BY F.FILM_ID ORDER BY COUNT(USER_ID) DESC LIMIT ?
   ````

#### USERS

1. GET /users Получение списка всех пользователей
   ````
   SELECT USER_ID, EMAIL, LOGIN, USER_NAME, BIRTHDAY FROM USERS
   ````
2. GET /users/{id} Получение пользователя по id
   ````
   SELECT USER_ID, EMAIL, LOGIN, USER_NAME, BIRTHDAY FROM USERS WHERE USER_ID = ?
   ````
3. POST /users Добавление нового пользователя
   ````
   INSERT INTO USERS (EMAIL, LOGIN, USER_NAME, BIRTHDAY) VALUES (?, ?, ?, ?)
   ````
4. PUT /users Обновление данных существующего пользователя
   ````
   UPDATE USERS SET EMAIL = ?, LOGIN = ?, USER_NAME = ?, BIRTHDAY = ? WHERE USER_ID = ?
   ````
5. DELETE /users/{id} Удаление пользователя по id
   ````
   DELETE FROM USERS WHERE USER_ID=?
   ````
6. PUT /users/{userId}/friends/{friendId} Запрос дружбы между пользователями
   ````
   INSERT INTO FRIENDS (USER_ID, FRIEND_ID) VALUES (?, ?)
   ````
7. DELETE /users/{userId}/friends/{friendId} Удаление запроса дружбы
   ````
   DELETE FROM FRIENDS WHERE (USER_ID = ? AND FRIEND_ID = ? )
   ````
8. GET users/{userId} Получение списка друзей пользователя
   ````
   SELECT FRIEND_ID FROM FRIENDS WHERE USER_ID = ?
   ````
9. GET users/{id}/friends/common/{otherId} Получение списка общих друзей для двух пользователей
   ````
   SELECT FirstUserFriends.id FROM (SELECT FRIEND_ID id FROM friends WHERE USER_ID = ? UNION " +
                "SELECT USER_ID UserId FROM friends WHERE FRIEND_ID = ?) AS FirstUserFriends " +
                "JOIN (SELECT FRIEND_ID id FROM friends WHERE USER_ID = ? UNION" +
                " SELECT USER_ID UserId FROM friends WHERE FRIEND_ID = ?) AS" +
                " SecondUserFriends ON FirstUserFriends.id = SecondUserFriends.id
   ````
   
#### GENRES

1. GET /genres Получение списка всех жанров
   ````
   SELECT * FROM GENRE WHERE GENRE.ID IN " +
                "(SELECT FILM_GENRES.GENRE_ID FROM FILM_GENRES WHERE FILM_ID=?)
   ````
2. GET /genres/{id} Получение жанра по id
   ````
   SELECT * FROM GENRE WHERE ID=?
   ````
3.    

#### MPA

1. GET /mpa Получение списка всех рейтингов
   ````
   SELECT * FROM MPA
   ````
2. GET /mpa/{id} Получение рейтинга по id
   ````
   SELECT * FROM MPA WHERE MPA_ID = ?
   ````
