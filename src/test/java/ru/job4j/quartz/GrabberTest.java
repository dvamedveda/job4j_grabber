package ru.job4j.quartz;

import org.junit.Assert;
import org.junit.Test;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import ru.job4j.html.Parse;
import ru.job4j.html.SqlRuParse;
import ru.job4j.store.Store;

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
        Store store = grabber.store(false);
        Scheduler scheduler = grabber.scheduler();
        grabber.init(parser, store, scheduler);
        Thread.sleep(10000);
        Assert.assertFalse(store.getAll().size() == 0);
    }
}