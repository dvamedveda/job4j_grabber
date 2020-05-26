package ru.job4j.quartz;

import org.junit.Assert;
import org.junit.Test;

import java.sql.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Properties;

import static org.hamcrest.core.Is.is;

/**
 * Тесты класса AlertRabbit.
 */
public class AlertRabbitTest {

    /**
     * Здесь проверяется правильный результат работы quartz-job'а.
     *
     * @throws SQLException ошибки при работе с бд.
     */
    @Test
    public void whenMakeRabbitThenCreatesRightEntries() throws SQLException {
        Properties properties = Config.loadProperties("test_db.properties");
        Database db = new Database(true);
        Connection workConnection = db.getConnection(properties);
        int interval = 10;
        AlertRabbit alertRabbit = new AlertRabbit();
        alertRabbit.makeRabbit(workConnection, interval);
        try (Connection testConnection = db.getConnection(properties)) {
            try (PreparedStatement search = testConnection.prepareStatement("select * from rabbit")) {
                try (ResultSet resultSet = search.executeQuery()) {
                    resultSet.next();
                    Instant timeOne = resultSet.getTimestamp("created_date").toInstant().truncatedTo(ChronoUnit.SECONDS);
                    resultSet.next();
                    Instant timeTwo = resultSet.getTimestamp("created_date").toInstant().truncatedTo(ChronoUnit.SECONDS);
                    Assert.assertThat(timeTwo.toEpochMilli() - timeOne.toEpochMilli(), is((long) 10000));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try (PreparedStatement deleteQuery = testConnection.prepareStatement("delete from rabbit")) {
                    deleteQuery.executeUpdate();
                }
            }
        }
    }
}