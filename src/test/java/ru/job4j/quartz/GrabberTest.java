package ru.job4j.quartz;

import org.junit.Assert;
import org.junit.Test;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import ru.job4j.html.Parse;
import ru.job4j.html.Post;
import ru.job4j.html.SqlRuParse;
import ru.job4j.html.SqlRuPost;
import ru.job4j.store.PgSqlStore;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Date;
import java.util.Properties;

import static org.hamcrest.core.Is.is;

/**
 * Тесты класса Grabber.
 */
public class GrabberTest {

    /**
     * Проверяется сохранение объекта настроек приложения.
     */
    @Test
    public void whenLoadCfgThenItLoadsCorrect() {
        Grabber grabber = new Grabber();
        grabber.setCfg("test.properties");
        Properties result = grabber.getCfg();
        Assert.assertThat(result.getProperty("rabbit"), is("1"));
    }

    /**
     * Проверяется выполнение задачи планировщиком за одну итерацию.
     *
     * @throws SchedulerException   ошибки работы планировщика.
     * @throws InterruptedException ошибка работы теста.
     */
    @Test
    public void whenInitSchedulerThenStarting() throws SchedulerException, InterruptedException {
        Grabber grabber = new Grabber();
        grabber.setCfg("test_db.properties");
        Parse parser = new SqlRuParse();
        try (PgSqlStore store = (PgSqlStore) grabber.store(false)) {
            Scheduler scheduler = grabber.scheduler();
            grabber.init(parser, store, scheduler);
            Thread.sleep(10000);
            Assert.assertFalse(store.getAll().size() == 0);
            scheduler.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Проверка выдачи найденных вакансий в браузер.
     */
    @Test
    public void whenWebStartsThenAnswerOk() {
        Grabber grabber = new Grabber();
        grabber.setCfg("test_db.properties");
        StringBuilder answer = new StringBuilder();
        try (PgSqlStore store = (PgSqlStore) grabber.store(false)) {
            Post post = new SqlRuPost();
            post.setAuthor("author");
            post.setUrl("url");
            post.setSummary("summary");
            post.setDescription("description");
            post.setCreateDate(11000L);
            post.setLastUpdateDate(22000L);
            store.save(post);
            grabber.web(store);
            Thread.sleep(3000);
            try (Socket socket = new Socket("127.0.0.1", 8013)) {
                Thread.sleep(3000);
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while (reader.ready()) {
                    answer.append(reader.readLine());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String expected = new StringBuilder()
                .append("HTTP/1.1 200 OK").append("url").append(new Date(11000L)).append("author")
                .append("summary").append("description").append(new Date(22000L)).toString();
        Assert.assertThat(answer.toString(), is(expected));
    }
}