package by.logonuk.repository.user;

import by.logonuk.domain.User;
import by.logonuk.exception.NoSuchEntityException;
import by.logonuk.util.DatabasePropertiesReader;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.util.*;

import static by.logonuk.repository.user.UserTableColumns.BIRTH_DATE;
import static by.logonuk.repository.user.UserTableColumns.CHANGED;
import static by.logonuk.repository.user.UserTableColumns.CREATED;
import static by.logonuk.repository.user.UserTableColumns.ID;
import static by.logonuk.repository.user.UserTableColumns.NAME;
import static by.logonuk.repository.user.UserTableColumns.SURNAME;
import static by.logonuk.repository.user.UserTableColumns.WEIGHT;

import static by.logonuk.util.DatabasePropertiesReader.POSTRGES_DRIVER_NAME;
import static by.logonuk.util.DatabasePropertiesReader.DATABASE_URL;
import static by.logonuk.util.DatabasePropertiesReader.DATABASE_PORT;
import static by.logonuk.util.DatabasePropertiesReader.DATABASE_NAME;
import static by.logonuk.util.DatabasePropertiesReader.DATABASE_LOGIN;
import static by.logonuk.util.DatabasePropertiesReader.DATABASE_PASSWORD;

public class UserRepository implements UserRepositoryInterface {

    @Override
    public User findById(Long id) {
        final String findByIdQuery = "select * from test_schema.users where id = " + id;

        Connection connection;
        Statement statement;
        ResultSet rs;

        try {
            connection = getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(findByIdQuery);
            boolean hasRow = rs.next();
            if (hasRow) {
                return userRowMapping(rs);
            } else {
                throw new NoSuchEntityException("Entity User with id " + id + " does not exist", 404);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException("SQL Issues!");
        }
    }

    @Override
    public List<User> findByLine(String line) {
        final String findByLineQuery = "select * from test_schema.users where user_name like '%" + line + "%' or surname like '%" + line + "%';";

        List<User> result = new ArrayList<>();

        Connection connection;
        Statement statement;
        ResultSet rs;

        try {
            connection = getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(findByLineQuery);

            while (rs.next()) {
                result.add(userRowMapping(rs));
            }

            return result;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException("SQL Issues!");
        }
    }

    @Override
    public Optional<User> findOne(Long id) {
        return Optional.of(findById(id));
    }

    public List<User> findAll() {
        return findAll(DEFAULT_FIND_ALL_LIMIT, DEFAULT_FIND_ALL_OFFSET);
    }

    private Connection getConnection() throws SQLException {
        try {
            Class.forName(DatabasePropertiesReader.getProperties(POSTRGES_DRIVER_NAME));
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver Cannot be loaded!");
            throw new RuntimeException("JDBC Driver Cannot be loaded!");
        }

        String url = DatabasePropertiesReader.getProperties(DATABASE_URL);
        String port = DatabasePropertiesReader.getProperties(DATABASE_PORT);
        String dbname = DatabasePropertiesReader.getProperties(DATABASE_NAME);
        String login = DatabasePropertiesReader.getProperties(DATABASE_LOGIN);
        String password = DatabasePropertiesReader.getProperties(DATABASE_PASSWORD);

        String jdbcURL = StringUtils.join(url, port, dbname);

        return DriverManager.getConnection(jdbcURL, login, password);
    }

    private User userRowMapping(ResultSet rs) throws SQLException {
        return User.builder()
                .id(rs.getLong(ID))
                .userName(rs.getString(NAME))
                .surname(rs.getString(SURNAME))
                .birth(rs.getTimestamp(BIRTH_DATE))
                .creationDate(rs.getTimestamp(CREATED))
                .modificationDate(rs.getTimestamp(CHANGED))
                .weight(rs.getDouble(WEIGHT))
                .build();
    }

    @Override
    public List<User> findAll(int limit, int offset) {
        final String findAllQuery = "select * from test_schema.users order by id limit " + limit + " offset " + offset;

        List<User> result = new ArrayList<>();

        Connection connection;
        Statement statement;
        ResultSet rs;

        try {
            connection = getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(findAllQuery);

            while (rs.next()) {
                result.add(userRowMapping(rs));
            }

            return result;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException("SQL Issues!");
        }
    }

    @Override
    public User create(User object) {
        return null;
    }

    @Override
    public User update(User object) {
        final String updateQuery =
                "update carshop.users " +
                        "set " +
                        "user_name = ?, surname = ?, birth = ?, is_deleted = ?, creation_date = ?, modification_date = ?, weight = ? " +
                        " where id = ?";

        Connection connection;
        PreparedStatement statement;

        try {
            connection = getConnection();
            statement = connection.prepareStatement(updateQuery);

            statement.setString(1, object.getUserName());
            statement.setString(2, object.getSurname());
            statement.setTimestamp(3, object.getBirth());
            statement.setBoolean(4, object.isDeleted());
            statement.setTimestamp(5, object.getCreationDate());
            statement.setTimestamp(6, object.getModificationDate());
            statement.setDouble(7, object.getWeight());
            statement.setLong(8, object.getId());

            statement.executeUpdate();

            return findById(object.getId());
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException("SQL Issues!");
        }
    }

    @Override
    public Long delete(Long id) {
        final String deleteQuery =
                "delete from carshop.users where id = ?";

        Connection connection;
        PreparedStatement statement;

        try {
            connection = getConnection();
            statement = connection.prepareStatement(deleteQuery);
            statement.setLong(1, id);
            statement.executeUpdate();

            return id;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException("SQL Issues!");
        }
    }

    @Override
    public Map<String, Object> getUserStats() {
        final String callFunction =
                "select carshop.get_users_stats_average_weight(?)";

        Connection connection;
        PreparedStatement statement;

        try {
            connection = getConnection();
            statement = connection.prepareStatement(callFunction);
            statement.setBoolean(1, true);
            ResultSet resultSet = statement.executeQuery();

            resultSet.next();
            double functiionCall = resultSet.getDouble(1);

            return Collections.singletonMap("avg", functiionCall);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException("SQL Issues!");
        }
    }
}
