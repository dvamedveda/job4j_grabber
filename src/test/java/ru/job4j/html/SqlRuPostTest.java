package ru.job4j.html;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.core.Is.is;

/**
 * В этом классе проверяются методы класса SqlRuPost
 */
public class SqlRuPostTest {

    /**
     * Здесть проверяется сравнение поста с сами собой.
     */
    @Test
    public void whenCompareSimilarPostThenIsEquals() {
        SqlRuPost post = new SqlRuPost();
        post.setUrl("https://www.sql.ru/forum/484798/pravila-foruma");
        post.setAuthor("author");
        post.setSummary("summary");
        post.setDescription("description");
        post.setCreateDate(0L);
        post.setLastUpdateDate(0L);
        Assert.assertThat(post.equals(post), is(true));
    }

    /**
     * Проверка сравнения поста с другим постом.
     */
    @Test
    public void whenCompareDifferentPostThenIsNotEquals() {
        SqlRuPost post1 = new SqlRuPost();
        post1.setUrl("https://www.sql.ru/forum/484798/pravila-foruma1");
        post1.setAuthor("author");
        post1.setSummary("summary");
        post1.setDescription("description");
        post1.setCreateDate(0L);
        post1.setLastUpdateDate(0L);
        SqlRuPost post2 = new SqlRuPost();
        post2.setUrl("https://www.sql.ru/forum/484798/pravila-foruma2");
        post2.setAuthor("author");
        post2.setSummary("summary");
        post2.setDescription("description");
        post2.setCreateDate(0L);
        post2.setLastUpdateDate(0L);
        Assert.assertThat(post1.equals(post2), is(false));
    }

    /**
     * Проверка добавления комментариев в пост.
     */
    @Test
    public void whenAddCommentThenAddSuccess() {
        SqlRuPost post = new SqlRuPost();
        post.setUrl("https://www.sql.ru/forum/484798/pravila-foruma");
        post.setAuthor("author");
        post.setSummary("summary");
        post.setDescription("description");
        post.setCreateDate(0L);
        post.setLastUpdateDate(0L);
        SqlRuComment comment = new SqlRuComment();
        comment.setAuthor("author1");
        comment.setDate(111L);
        comment.setText("some text");
        post.addComment(comment);
        List<SqlRuComment> comments = post.getComments();
        Assert.assertThat(comments.size(), is(1));
        SqlRuComment result = comments.get(0);
        Assert.assertThat(result.getAuthor(), is("author1"));
        Assert.assertThat(result.getText(), is("some text"));
        Assert.assertThat(result.getDate(), is(111L));
    }
}