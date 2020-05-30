package ru.job4j.html;

import java.util.Objects;

/**
 * Класс, описывающий модель данных для комментария к посту sql.ru.
 */
public class SqlRuComment {
    private String text;
    private String author;
    private long date;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SqlRuComment that = (SqlRuComment) o;
        return this.hashCode() == that.hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, author, date);
    }
}