package ru.job4j.html;

/**
 * Интерфейс, описывающий модель данных для поста.
 */
public interface Post {
    void setAuthor(String author);

    void setUrl(String url);

    void setDescription(String description);

    void setSummary(String summary);

    void setCreateDate(long date);

    void setLastUpdateDate(long date);

    String getAuthor();

    String getUrl();

    String getDescription();

    String getSummary();

    long getCreateDate();

    long getLastUpdateDate();

    boolean equals(Object o);

    int hashCode();

    String toString();
}