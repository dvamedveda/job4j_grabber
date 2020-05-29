package ru.job4j.html;

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
}