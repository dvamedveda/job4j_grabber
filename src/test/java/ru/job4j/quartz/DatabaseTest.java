package ru.job4j.quartz;

import org.junit.Assert;
import org.junit.Test;

import java.sql.*;
import java.util.Properties;

import static org.hamcrest.core.Is.is;

/**
 * Тесты класса Database.
 */
public class DatabaseTest {

    /**
     * Здесь проверяется создание соединения с базой данных.
     *
     * @throws SQLException ошибки при работе с бд.
     */
    @Test
    public void whenCreateConnectionThenSuccess() throws SQLException {
        Properties properties = Config.loadProperties("test_db.properties");
        long time = System.currentTimeMillis();
        try (Connection connection = new Database(false).getConnection(properties)) {
            try (PreparedStatement query = connection.prepareStatement("insert into rabbit(created_date) values (?)")) {
                query.setTimestamp(1, new Timestamp(time));
                query.executeUpdate();
            }
            try (PreparedStatement search = connection.prepareStatement("select * from rabbit where created_date = ?")) {
                search.setTimestamp(1, new Timestamp(time));
                try (ResultSet resultSet = search.executeQuery()) {
                    resultSet.next();
                    Assert.assertThat(resultSet.getTimestamp("created_date").getTime(), is(time));
                }
            }
            connection.rollback();
        }

    }
}