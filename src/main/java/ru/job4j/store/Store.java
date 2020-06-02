package ru.job4j.store;

import ru.job4j.html.Post;

import java.util.List;

/**
 * Интерфейс, описывающий хранилище постов.
 */
public interface Store {
    void save(Post post);

    List<Post> getAll();

    Post findById(String id);
}