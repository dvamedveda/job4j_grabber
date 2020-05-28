package ru.job4j.html;

import org.junit.Assert;
import org.junit.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;

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
        String someDate = "20 май 20, 14:13";
        Long dateInMillis = SqlRuParse.parseDate(someDate);
        Assert.assertThat(dateInMillis, is(1589983980000L));
    }

    /**
     * Здесь проверяется парсинг относительного значения даты - сегодня.
     */
    @Test
    public void whenParseTodayThenParsingCorrect() {
        String someData = "сегодня, 12:34";
        LocalTime time = LocalTime.parse(" 12:34", DateTimeFormatter.ofPattern(" HH:mm"));
        LocalDate date = LocalDate.now();
        LocalDateTime dateTime = LocalDateTime.of(date, time);
        long result = SqlRuParse.parseDate(someData);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(dateTime, ZoneId.of("Z"));
        long expected = zonedDateTime.toInstant().toEpochMilli();
        Assert.assertThat(result, is(expected));
    }

    /**
     * Здесь проверяется парсинг относительного значения даты - вчера.
     */
    @Test
    public void whenParseYesterdaydayThenParsingCorrect() {
        String someData = "вчера, 12:34";
        LocalTime time = LocalTime.parse(" 12:34", DateTimeFormatter.ofPattern(" HH:mm"));
        LocalDate date = LocalDate.now().minusDays(1);
        LocalDateTime dateTime = LocalDateTime.of(date, time);
        long result = SqlRuParse.parseDate(someData);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(dateTime, ZoneId.of("Z"));
        long expected = zonedDateTime.toInstant().toEpochMilli();
        Assert.assertThat(result, is(expected));
    }
}