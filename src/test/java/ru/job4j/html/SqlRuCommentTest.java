package ru.job4j.html;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.core.Is.is;

/**
 * В этом классе тестируются методы класса SqlRuComment
 */
public class SqlRuCommentTest {

    /**
     * Сравнение комментария с самим собой.
     */
    @Test
    public void whenCompareOneCommentThenEquals() {
        SqlRuComment comment = new SqlRuComment();
        comment.setAuthor("author");
        comment.setText("text");
        comment.setDate(0L);
        Assert.assertThat(comment.equals(comment), is(true));
    }

    /**
     * Сравнение комментария с другим комментарием.
     */
    @Test
    public void whenCompareDifferentCommentThenNotEquals() {
        SqlRuComment comment1 = new SqlRuComment();
        comment1.setAuthor("author1");
        comment1.setText("text1");
        comment1.setDate(1L);
        SqlRuComment comment2 = new SqlRuComment();
        comment2.setAuthor("author2");
        comment2.setText("text2");
        comment2.setDate(2L);
        Assert.assertThat(comment1.equals(comment2), is(false));
    }

    /**
     * Сравнение комментария с null.
     */
    @Test
    public void whenCompareWithNullThenNotEquals() {
        SqlRuComment comment = new SqlRuComment();
        comment.setAuthor("author");
        comment.setText("text");
        comment.setDate(0L);
        Assert.assertThat(comment.equals(null), is(false));
    }
}