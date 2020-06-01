package ru.job4j.html;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Точка запуска программы.
 */
public class Start {
    public static void main(String[] args) {
        SqlRuParse parser = new SqlRuParse();
        List<Post> posts = new ArrayList<>();
        posts.addAll(parser.list("https://www.sql.ru/forum/job-offers/1"));
        posts.addAll(parser.list("https://www.sql.ru/forum/job-offers/2"));
        posts.addAll(parser.list("https://www.sql.ru/forum/job-offers/3"));
        posts.addAll(parser.list("https://www.sql.ru/forum/job-offers/4"));
        posts.addAll(parser.list("https://www.sql.ru/forum/job-offers/5"));
        for (Post post : posts) {
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
    }
}