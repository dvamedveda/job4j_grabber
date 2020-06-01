-- Создание таблицы post для сохранения постов в базу данных.
create table post
(
    id           serial primary key,
    summary      character varying(300),
    author       character varying(30),
    created      timestamp(0),
    last_updated timestamp(0),
    url          character varying(300) unique,
    description  text
)