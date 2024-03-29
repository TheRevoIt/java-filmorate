create table if not exists USERS
(
    USER_ID   INT PRIMARY KEY AUTO_INCREMENT,
    EMAIL     VARCHAR(255) NOT NULL,
    LOGIN     VARCHAR(255) NOT NULL,
    USER_NAME VARCHAR(255) NOT NULL,
    BIRTHDAY  DATE
);
create table if not exists FILMS
(
    FILM_ID      INT PRIMARY KEY AUTO_INCREMENT,
    TITLE        VARCHAR(255) NOT NULL,
    DESCRIPTION  VARCHAR(255) NOT NULL,
    RELEASE_DATE DATE,
    DURATION     INTEGER
        CHECK (DURATION > 0),
    MPA_ID       INTEGER      NOT NULL
);
create table if not exists GENRE
(
    ID   int primary key auto_increment,
    NAME VARCHAR(255) NOT NULL UNIQUE
);
create table if not exists FILM_GENRES
(
    FILM_ID  INT NOT NULL ,
    GENRE_ID INT NOT NULL ,
    FOREIGN KEY (FILM_ID) REFERENCES FILMS (FILM_ID) ON DELETE CASCADE,
    FOREIGN KEY (GENRE_ID) REFERENCES GENRE (ID) ON DELETE CASCADE,
    PRIMARY KEY (FILM_ID, GENRE_ID)
);
create table if not exists MPA
(
    mpa_id int          not null primary key auto_increment,
    name   VARCHAR(255) NOT NULL,
    constraint MPA_PK primary key (mpa_id)
);
create table if not exists LIKES
(
    FILM_ID INT NOT NULL ,
    USER_ID INT NOT NULL ,
    FOREIGN KEY (FILM_ID) REFERENCES FILMS (FILM_ID) ON DELETE CASCADE,
    FOREIGN KEY (USER_ID) REFERENCES USERS (USER_ID) ON DELETE CASCADE
);
create table if not exists FRIENDS
(
    USER_ID INT NOT NULL ,
    FRIEND_ID INT NOT NULL ,
    FOREIGN KEY (USER_ID) REFERENCES USERS (USER_ID) ON DELETE CASCADE,
    FOREIGN KEY (FRIEND_ID) REFERENCES USERS (USER_ID) ON DELETE CASCADE
);
