package ru.job4j.store;

import ru.job4j.html.Parse;
import ru.job4j.html.Post;
import ru.job4j.html.SqlRuParse;
import ru.job4j.quartz.Config;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Класс для демонстрации работы класса PgSqlStore.
 */
public class Demo {
    public static void main(String[] args) {
        Properties config = Config.loadProperties("rabbit.properties");
        Parse parser = new SqlRuParse();
        List<Post> posts = new ArrayList<>();
        posts.addAll(parser.list("https://www.sql.ru/forum/job-offers/1"));
        posts.addAll(parser.list("https://www.sql.ru/forum/job-offers/2"));
        posts.addAll(parser.list("https://www.sql.ru/forum/job-offers/3"));
        posts.addAll(parser.list("https://www.sql.ru/forum/job-offers/4"));
        posts.addAll(parser.list("https://www.sql.ru/forum/job-offers/5"));
        try (PgSqlStore store = new PgSqlStore(config, true)) {
            for (Post post : posts) {
                store.save(post);
            }
            List<Post> result = store.getAll();
            for (Post post : result) {
                System.out.println(post.getUrl());
                System.out.println(post.getAuthor());
                System.out.println(post.getCreateDate());
                System.out.println(Instant.ofEpochMilli(post.getCreateDate()));
                System.out.println(post.getSummary());
                System.out.println(post.getDescription());
                System.out.println(post.getLastUpdateDate());
                System.out.println(Instant.ofEpochMilli(post.getLastUpdateDate()));
                System.out.println("----------------------------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}