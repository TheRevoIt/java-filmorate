merge into MPA(mpa_id, name)
values (1, 'G');
merge into MPA(mpa_id, name)
    values (2, 'PG');
merge into MPA(mpa_id, name)
    values (3, 'PG-13');
merge into MPA(mpa_id, name)
    values (4, 'R');
merge into MPA(mpa_id, name)
    values (5, 'NC-17');
--Заполнение справочников
merge into GENRE(id, name)
    values (1, 'Комедия');
merge into GENRE(id, name)
    values (2, 'Драма');
merge into GENRE(id, name)
    values (3, 'Мультфильм');
merge into GENRE(id, name)
    values (4, 'Триллер');
merge into GENRE(id, name)
    values (5, 'Документальный');
merge into GENRE(id, name)
    values (6, 'Боевик');
