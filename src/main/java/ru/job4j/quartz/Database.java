package ru.job4j.quartz;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Вспомогательный класс для работы с базой данных.
 */
public class Database {

    /**
     * Настройки автокоммита для гибкого создания соединений.
     */
    boolean autocommit;

    public Database(boolean autocommit) {
        this.autocommit = autocommit;
    }

    /**
     * Получить соединение с бд по заданным настройкам.
     *
     * @param properties файл настроек.
     * @return объект соединения.
     */
    public Connection getConnection(Properties properties) {
        Connection result = null;
        try {
            Class.forName(properties.getProperty("driver-class-name"));
            result = DriverManager.getConnection(
                    properties.getProperty("url"),
                    properties.getProperty("username"),
                    properties.getProperty("password")
            );
            result.setAutoCommit(autocommit);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}