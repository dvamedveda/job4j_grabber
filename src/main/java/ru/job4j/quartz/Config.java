package ru.job4j.quartz;

import java.io.InputStream;
import java.util.Properties;

/**
 * Класс для работы с конфигом Quartz.
 */
public class Config {

    /**
     * Загрузить конфиг из файла.
     *
     * @param name имя файла.
     * @return файл Properties.
     */
    public static Properties loadProperties(String name) {
        Properties result = null;
        try (InputStream inputStream = Config.class.getClassLoader().getResourceAsStream(name)) {
            Properties properties = new Properties();
            properties.load(inputStream);
            result = properties;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}