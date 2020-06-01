package ru.job4j.store;

import ru.job4j.html.Post;

import java.util.List;

public interface Store {
    void save(Post post);

    List<Post> getAll();
}