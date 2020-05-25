package ru.job4j.quartz;

import org.junit.Assert;
import org.junit.Test;

import java.util.Properties;

import static org.hamcrest.core.Is.is;

public class ConfigTest {

    /**
     * В этом тесте проверяется загрузка файла настроек и получение существующего свойства.
     */
    @Test
    public void whenLoadConfigAndGetExistPropertyThenSuccess() {
        Properties properties = Config.loadProperties("test.properties");
        String expected = "1";
        Assert.assertThat(properties.getProperty("rabbit"), is(expected));
    }

    /**
     * В этом тесте проверяется загрузка файла настроек и получение несуществующего свойства.
     */
    @Test
    public void whenLoadConfigAndGetUnExistPropertyThenFail() {
        Properties properties = Config.loadProperties("test.properties");
        String expected = null;
        Assert.assertThat(properties.getProperty("alert"), is(expected));
    }
}