package ru.job4j.html;

import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;

/**
 * Тесты класса SqlRuParse.
 */
public class SqlRuParseTest {

    /**
     * Здесь проверяется парсинг абсолютного значения даты.
     */
    @Test
    public void whenParseDateThenParsingCorrect() {
        SqlRuParse parser = new SqlRuParse();
        String someDate = "20 май 20, 14:13";
        Long dateInMillis = parser.parseDate(someDate);
        Assert.assertThat(dateInMillis, is(1589983980000L));
    }

    /**
     * Здесь проверяется парсинг относительного значения даты - сегодня.
     */
    @Test
    public void whenParseTodayThenParsingCorrect() {
        SqlRuParse parser = new SqlRuParse();
        String someData = "сегодня, 12:34";
        LocalTime time = LocalTime.parse(" 12:34", DateTimeFormatter.ofPattern(" HH:mm"));
        LocalDate date = LocalDate.now();
        LocalDateTime dateTime = LocalDateTime.of(date, time);
        long result = parser.parseDate(someData);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(dateTime, ZoneId.of("Z"));
        long expected = zonedDateTime.toInstant().toEpochMilli();
        Assert.assertThat(result, is(expected));
    }

    /**
     * Здесь проверяется парсинг относительного значения даты - вчера.
     */
    @Test
    public void whenParseYesterdaydayThenParsingCorrect() {
        SqlRuParse parser = new SqlRuParse();
        String someData = "вчера, 12:34";
        LocalTime time = LocalTime.parse(" 12:34", DateTimeFormatter.ofPattern(" HH:mm"));
        LocalDate date = LocalDate.now().minusDays(1);
        LocalDateTime dateTime = LocalDateTime.of(date, time);
        long result = parser.parseDate(someData);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(dateTime, ZoneId.of("Z"));
        long expected = zonedDateTime.toInstant().toEpochMilli();
        Assert.assertThat(result, is(expected));
    }

    /**
     * Здесь проверяется получение топиков с первой страницы раздела вакансий sql.ru.
     */
    @Test
    public void whenGetTopicsFromFirstPageThenGetCorrect() {
        SqlRuParse parser = new SqlRuParse();
        List<Element> topics = new ArrayList<>(parser.getTopics("https://www.sql.ru/forum/job-offers/1"));
        Element href = topics.get(0).child(0);
        String resultUrl = href.attr("href");
        String resultSummary = href.text();
        Assert.assertThat(resultUrl, is("https://www.sql.ru/forum/484798/pravila-foruma"));
        Assert.assertThat(resultSummary, is("Правила форума"));
    }
}