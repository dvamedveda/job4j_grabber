package ru.job4j.html;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Класс, реализующий модель данных для топика sql.ru.
 */
public class SqlRuPost implements Post {
    private String summary;
    private String description;
    private String author;
    private long createDate;
    private long lastUpdateDate;
    private String url;
    private List<SqlRuComment> comments = new ArrayList<>();

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public long getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(long lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public void addComment(SqlRuComment comment) {
        this.comments.add(comment);
    }

    public List<SqlRuComment> getComments() {
        return this.comments;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SqlRuPost sqlRuPost = (SqlRuPost) o;
        return this.hashCode() == sqlRuPost.hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(url);
    }
}