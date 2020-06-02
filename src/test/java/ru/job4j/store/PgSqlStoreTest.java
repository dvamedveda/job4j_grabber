package ru.job4j.store;

import org.junit.Assert;
import org.junit.Test;
import ru.job4j.html.Parse;
import ru.job4j.html.Post;
import ru.job4j.html.SqlRuParse;
import ru.job4j.html.SqlRuPost;
import ru.job4j.quartz.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.hamcrest.core.Is.is;

/**
 * Тесты класса PgSqlStore.
 */
public class PgSqlStoreTest {

    /**
     * Проверка сохранения списка постов в бд и получения списка всех постов из бд.
     */
    @Test
    public void whenSavePostListAndGetAllThenCorrect() {
        Properties config = Config.loadProperties("test_db.properties");
        Parse parser = new SqlRuParse();
        List<Post> posts = new ArrayList<>();
        List<Post> result = new ArrayList<>();
        posts.addAll(parser.list("https://www.sql.ru/forum/job-offers/1"));
        try (PgSqlStore store = new PgSqlStore(config, false)) {
            for (Post post : posts) {
                store.save(post);
            }
            result = store.getAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertThat(result.size(), is(53));
        Assert.assertThat(result.get(0).getUrl(), is("https://www.sql.ru/forum/484798/pravila-foruma"));
        Assert.assertThat(result.get(0).getSummary(), is("Правила форума"));
        Assert.assertThat(result.get(0).getAuthor(), is("judge"));
        Assert.assertThat(result.get(0).getDescription(), is("Правилами форума временно считаются "
                + "правила форума Работа связанные с публикацией вакансий. "
                + "Сообщение было отредактировано: 17 окт 07, 11:30"));
    }

    /**
     * Проверка поиска поста в бд по идентификатору.
     */
    @Test
    public void whenSearchByIdThenFindCorrect() {
        Post post = new SqlRuPost();
        post.setUrl("url");
        post.setAuthor("author");
        post.setSummary("summary");
        post.setDescription("description");
        post.setCreateDate(12345L);
        post.setLastUpdateDate(54321L);
        Post result = null;
        List<String> ids = null;
        Properties config = Config.loadProperties("test_db.properties");
        try (PgSqlStore store = new PgSqlStore(config, false)) {
            store.save(post);
            ids = store.getIds();
            result = store.findById(ids.get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertThat(ids.size(), is(1));
        Assert.assertThat(result.getUrl(), is("url"));
        Assert.assertThat(result.getAuthor(), is("author"));
        Assert.assertThat(result.getSummary(), is("summary"));
        Assert.assertThat(result.getDescription(), is("description"));
        Assert.assertThat(result.getCreateDate(), is(12000L));
        Assert.assertThat(result.getLastUpdateDate(), is(54000L));
    }
}