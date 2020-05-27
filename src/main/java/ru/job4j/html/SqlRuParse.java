package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Парсинг страницы сайта https://www.sql.ru
 */
public class SqlRuParse {
    public static void main(String[] args) throws IOException {
        Document document = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
        Elements rows = document.select(".postslisttopic");
        for (Element row : rows) {
            Element href = row.child(0);
            System.out.println(href.attr("href"));
            System.out.println(href.text());
            Element date = row.parent().child(5);
            System.out.println(date.text());
            System.out.println();
        }
    }
}