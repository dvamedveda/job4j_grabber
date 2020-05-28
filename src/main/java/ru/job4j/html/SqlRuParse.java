package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

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
            System.out.println(parseDate(date.text()));
            Instant instant = Instant.ofEpochMilli(parseDate(date.text()));
            System.out.println(instant);
            System.out.println();
        }
    }

    /**
     * Метод для преобразования даты в формате sql.ru в unix-time.
     *
     * @param textDate дата формата sql.ru
     * @return unix-time
     */
    public static long parseDate(String textDate) {
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
    public static String getMonthNumber(String month) {
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