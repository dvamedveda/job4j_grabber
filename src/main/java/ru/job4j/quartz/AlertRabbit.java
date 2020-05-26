package ru.job4j.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Properties;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Класс содержит реализацию планировщика заданий.
 */
public class AlertRabbit {

    /**
     * Конфигурация и точка запуска программы.
     *
     * @param args
     */
    public static void main(String[] args) {
        AlertRabbit rabbit = new AlertRabbit();
        Properties properties = Config.loadProperties("rabbit.properties");
        int interval = Integer.parseInt(properties.getProperty("rabbit.interval"));
        Connection connection = new Database(true).getConnection(properties);
        rabbit.makeRabbit(connection, interval);
    }

    /**
     * Запуск задачи кварца с установленными настройками.
     *
     * @param connection соединение для выполнения задачи кварца.
     * @param interval   периодичность выполнения задачи.
     */
    public void makeRabbit(Connection connection, Integer interval) {
        try (Connection database = connection) {
            JobDataMap dataMap = new JobDataMap();
            dataMap.put("database", database);
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDetail job = newJob(Rabbit.class).usingJobData(dataMap).build();
            SimpleScheduleBuilder time = simpleSchedule().withIntervalInSeconds(interval).repeatForever();
            Trigger trigger = newTrigger().startNow().withSchedule(time).build();
            scheduler.scheduleJob(job, trigger);
            Thread.sleep(10000);
            scheduler.shutdown();
        } catch (SQLException | SchedulerException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Реализация задачи кварца.
     * Вставляет записи в бд, содержащие текущее время.
     */
    public static class Rabbit implements Job {

        public Rabbit() {
            System.out.println(this.hashCode());
        }

        @Override
        public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
            System.out.println("Rabbit runs here...");
            Connection connection = (Connection) jobExecutionContext.getJobDetail().getJobDataMap().get("database");
            try (PreparedStatement query = connection.prepareStatement(
                    "insert into rabbit(created_date) values (?)")) {
                query.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                query.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}