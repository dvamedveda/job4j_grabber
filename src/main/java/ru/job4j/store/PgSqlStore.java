package ru.job4j.store;

import org.postgresql.util.PSQLException;
import ru.job4j.html.Post;
import ru.job4j.html.SqlRuPost;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * В классе реализуется хранилище с использованием базы данных PostgreSQL
 */
public class PgSqlStore implements Store, AutoCloseable {

    /**
     * Сохраненное соединение.
     */
    private Connection connection;

    /**
     * Конструктор хранилища.
     *
     * @param properties свойства подключения.
     * @param autoCommit флажок для более гибкой работы с соединением.
     */
    public PgSqlStore(Properties properties, boolean autoCommit) {
        try {
            Class.forName(properties.getProperty("driver-class-name"));
            connection = DriverManager.getConnection(
                    properties.getProperty("url"),
                    properties.getProperty("username"),
                    properties.getProperty("password")
            );
            connection.setAutoCommit(autoCommit);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Закрытие соединения. Если соединение используется в тестах, то откатываются изменения.
     *
     * @throws Exception
     */
    @Override
    public void close() throws Exception {
        if (connection != null) {
            if (!connection.getAutoCommit()) {
                connection.rollback();
            }
            connection.close();
        }
    }

    /**
     * Сохранение поста в бд.
     *
     * @param post объект поста.
     */
    @Override
    public void save(Post post) {
        try (PreparedStatement statement = this.connection.prepareStatement(
                "insert into post(summary, author, created, last_updated, url, description) "
                        + "values (?, ?, ?, ?, ?, ?)"
        )) {
            statement.setString(1, post.getSummary());
            statement.setString(2, post.getAuthor());
            statement.setTimestamp(3, new Timestamp(post.getCreateDate()));
            statement.setTimestamp(4, new Timestamp(post.getLastUpdateDate()));
            statement.setString(5, post.getUrl());
            statement.setString(6, post.getDescription());
            statement.executeUpdate();
            System.out.println("Added one new post");
        } catch (PSQLException e) {
            System.out.println("Try to adding already existing post, skipping it.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Получение всех постов из бд.
     *
     * @return список объектов постов.
     */
    @Override
    public List<Post> getAll() {
        List<Post> result = new ArrayList<>();
        try (PreparedStatement statement = this.connection.prepareStatement("select * from post")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Post post = new SqlRuPost();
                    post.setSummary(resultSet.getString("summary"));
                    post.setAuthor(resultSet.getString("author"));
                    post.setCreateDate(resultSet.getTimestamp("created").getTime());
                    post.setLastUpdateDate(resultSet.getTimestamp("last_updated").getTime());
                    post.setUrl(resultSet.getString("url"));
                    post.setDescription(resultSet.getString("description"));
                    result.add(post);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Поиск поста по его id.
     *
     * @param id идентификатор поста.
     * @return объект поста.
     */
    @Override
    public Post findById(String id) {
        Post result = new SqlRuPost();
        try (PreparedStatement statement = this.connection.prepareStatement("select * from post where id = ?")) {
            statement.setInt(1, Integer.parseInt(id));
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    result.setSummary(resultSet.getString("summary"));
                    result.setAuthor(resultSet.getString("author"));
                    result.setCreateDate(resultSet.getTimestamp("created").getTime());
                    result.setLastUpdateDate(resultSet.getTimestamp("last_updated").getTime());
                    result.setUrl(resultSet.getString("url"));
                    result.setDescription(resultSet.getString("description"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Получение списка идентификаторов постов, сохраненных в бд.
     *
     * @return список идентификаторов.
     */
    public List<String> getIds() {
        List<String> result = new ArrayList<>();
        try (PreparedStatement statement = this.connection.prepareStatement("select id from post")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    result.add(resultSet.getString("id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}