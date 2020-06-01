package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Парсинг страницы сайта https://www.sql.ru
 */
public class SqlRuParse implements Parse {

    /**
     * Метод для получения ссылки на пост из топика.
     *
     * @param topic элемент топика.
     * @return ссылка на пост с комментариями.
     */
    private String parseTopicForUrl(Element topic) {
        String result = "";
        Element href = topic.child(0);
        result = href.attr("href");
        return result;
    }

    /**
     * Метод для получения времени последнего обновления поста из топика.
     *
     * @param topic элемент топика.
     * @return время последнего обновления поста.
     */
    private long parseTopicForLastUpdateDate(Element topic) {
        long result;
        Element lastUpdateDate = topic.parent().child(5);
        result = this.parseDate(lastUpdateDate.text());
        return result;
    }

    /**
     * Метод для получения списка постов с вакансиями по ссылке на страницу с топиками с сайта sql.ru
     *
     * @param url ссылка на страницу топиков раздела вакансий.
     * @return список постов.
     */
    public List<Post> list(String url) {
        ArrayList<Post> result = new ArrayList<>();
        try {
            Document document = Jsoup.connect(url).get();
            Elements topics = document.select(".postslisttopic");
            for (Element topic : topics) {
                result.add(detail(parseTopicForUrl(topic), parseTopicForLastUpdateDate(topic)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Метод для получения объекта поста по ссылке.
     *
     * @param url            ссылка на пост из топика.
     * @param lastUpdateDate время последнего обновления поста для добавления в объект поста.
     * @return объект поста.
     */
    public Post detail(String url, long lastUpdateDate) {
        SqlRuPost result = new SqlRuPost();
        try {
            Document document = Jsoup.connect(url).get();
            Elements entries = document.select(".msgTable");
            Element post = entries.get(0);
            result.setSummary(post.child(0).child(0).child(0).ownText());
            result.setAuthor(post.child(0).child(1).child(0).text().split("\\s")[0]);
            result.setDescription(post.child(0).child(1).child(1).text());
            String created = post.child(0).child(2).child(0).ownText().split("\\s\\[")[0];
            result.setCreateDate(this.parseDate(created));
            result.setLastUpdateDate(lastUpdateDate);
            result.setUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Метод для преобразования даты в формате sql.ru в unix-time.
     *
     * @param textDate дата формата sql.ru
     * @return unix-time
     */
    public long parseDate(String textDate) {
        long result;
        Scanner byComma = new Scanner(textDate).useDelimiter(",");
        String stringDate = byComma.next();
        String stringTime = byComma.next();
        LocalDate date;
        LocalTime time;
        if (stringDate.equals("сегодня")) {
            date = LocalDate.now();
        } else if (stringDate.equals("вчера")) {
            date = LocalDate.now().minusDays(1);
        } else {
            Scanner bySpaces = new Scanner(stringDate).useDelimiter(" ");
            StringBuilder numbered = new StringBuilder()
                    .append(bySpaces.next()).append(" ")
                    .append(getMonthNumber(bySpaces.next())).append(" ")
                    .append(bySpaces.next());
            date = LocalDate.parse(numbered, DateTimeFormatter.ofPattern("d MM yy"));
        }
        time = LocalTime.parse(stringTime, DateTimeFormatter.ofPattern(" HH:mm"));
        LocalDateTime dateTime = LocalDateTime.of(date, time);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(dateTime, ZoneId.of("Z"));
        result = zonedDateTime.toInstant().toEpochMilli();
        return result;
    }

    /**
     * Метод для преобразования месяца в текстовом формате sql.ru в числовое значение.
     *
     * @param month месяц в текстовом формате sql.ru.
     * @return число, обозначающее месяц.
     */
    public String getMonthNumber(String month) {
        String result;
        switch (month) {
            case "янв":
                result = "01";
                break;
            case "фев":
                result = "02";
                break;
            case "мар":
                result = "03";
                break;
            case "апр":
                result = "04";
                break;
            case "май":
                result = "05";
                break;
            case "июн":
                result = "06";
                break;
            case "июл":
                result = "07";
                break;
            case "авг":
                result = "08";
                break;
            case "сен":
                result = "09";
                break;
            case "окт":
                result = "10";
                break;
            case "ноя":
                result = "11";
                break;
            case "дек":
                result = "12";
                break;
            default:
                result = "";
        }
        return result;
    }
}