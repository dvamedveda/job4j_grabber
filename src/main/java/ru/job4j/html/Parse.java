package ru.job4j.html;

import java.util.List;

/**
 * Интерфейс описывающий парсинг сайта с вакансиями.
 */
public interface Parse {
    List<Post> list(String url);

    Post detail(String url, long lastUpdated);
}