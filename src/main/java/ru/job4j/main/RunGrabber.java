package ru.job4j.main;

import liquibase.Liquibase;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import ru.job4j.html.SqlRuParse;
import ru.job4j.quartz.Config;
import ru.job4j.quartz.Database;
import ru.job4j.quartz.Grabber;
import ru.job4j.store.Store;

import java.io.IOException;
import java.sql.Connection;

/**
 * Главный класс для запуска приложения Grabber.
 * Он подготавливает базу данных для работы и запускает quartz-job, периодически выполняющий работу.
 */
public class RunGrabber {
    public static void main(String[] args) throws SchedulerException, IOException, LiquibaseException {
        Connection connection = new Database(true).getConnection(Config.loadProperties("rabbit.properties"));
        liquibase.database.Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
        Liquibase liquibase = new Liquibase("db/master.xml", new FileSystemResourceAccessor(), database);
        liquibase.update("");
        Grabber grabber = new Grabber();
        grabber.setCfg("rabbit.properties");
        Scheduler scheduler = grabber.scheduler();
        Store store = grabber.store(true);
        grabber.init(new SqlRuParse(), store, scheduler);
    }
}