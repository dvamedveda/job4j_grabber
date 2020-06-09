package ru.job4j.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import ru.job4j.html.Parse;
import ru.job4j.html.Post;
import ru.job4j.store.PgSqlStore;
import ru.job4j.store.Store;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Данный класс реализует интерфейс Grab для периодического выполнения задачи.
 */
public class Grabber implements Grab {

    /**
     * Поле для хранения настроек приложения.
     */
    private Properties properties = null;

    /**
     * Получение хранилища.
     *
     * @param autocommit разрешен ли автокоммит в хранилище.
     * @return объект хранилища.
     */
    public Store store(boolean autocommit) {
        return new PgSqlStore(this.properties, autocommit);
    }

    /**
     * Запуск планировщика заданий.
     *
     * @return планировщик заданий.
     * @throws SchedulerException ошибки запуска планировщика заданий.
     */
    public Scheduler scheduler() throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        return scheduler;
    }

    /**
     * Установить настройки приложения.
     *
     * @param name имя файла с настройками приложения.
     */
    public void setCfg(String name) {
        properties = Config.loadProperties(name);
    }

    /**
     * Получить настройки приложения.
     *
     * @return объект настроек.
     */
    public Properties getCfg() {
        return this.properties;
    }

    /**
     * Запуск задачи периодического получения объявлений.
     *
     * @param parse     объект парсера.
     * @param store     объект хранилища.
     * @param scheduler объект планировщика.
     * @throws SchedulerException ошибки работы планировщика.
     */
    @Override
    public void init(Parse parse, Store store, Scheduler scheduler) throws SchedulerException {
        JobDataMap dataMap = new JobDataMap();
        dataMap.put("store", store);
        dataMap.put("parse", parse);
        JobDetail jobDetail = newJob(GrabJob.class).usingJobData(dataMap).build();
        int interval = Integer.parseInt(properties.getProperty("rabbit.interval"));
        SimpleScheduleBuilder times = simpleSchedule().withIntervalInSeconds(interval).repeatForever();
        Trigger trigger = newTrigger().startNow().withSchedule(times).build();
        scheduler.scheduleJob(jobDetail, trigger);
    }

    /**
     * Запуск в отдельном потоке выдачи информации в браузер.
     * @param store хранилище заявок
     */
    public void web(Store store) {
        Runnable forWeb = new Runnable() {
            @Override
            public void run() {
                int answerPort = Integer.parseInt(properties.getProperty("rabbit.port"));
                try (ServerSocket serverSocket = new ServerSocket(answerPort)) {
                    while (!serverSocket.isClosed()) {
                        Socket socket = serverSocket.accept();
                        try (OutputStream writer = socket.getOutputStream()) {
                            writer.write("HTTP/1.1 200 OK\r\n\r\n".getBytes());
                            for (Post post : store.getAll()) {
                                writer.write(post.toString().getBytes(Charset.forName("Windows-1251")));
                                writer.write(System.lineSeparator().getBytes());
                            }
                            writer.flush();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread runner = new Thread(forWeb);
        runner.start();
    }

    /**
     * Внутренний класс, реализующий задачу получения объявлений.
     * Выполняется периодически и сохраняет новые объявления в бд,
     * если они подходят по условиям.
     */
    public static class GrabJob implements Job {

        public GrabJob() {
            System.out.println("Next iteration");
        }

        @Override
        public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
            JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
            Store store = (Store) jobDataMap.get("store");
            Parse parser = (Parse) jobDataMap.get("parse");
            List<Post> posts = new ArrayList<>();
            posts.addAll(parser.list("https://www.sql.ru/forum/job-offers/1"));
            for (Post post : posts) {
                String normalizedSummary = post.getSummary().toLowerCase();
                if (normalizedSummary.contains("java") && !normalizedSummary.contains("script")) {
                    store.save(post);
                }
            }
        }
    }
}