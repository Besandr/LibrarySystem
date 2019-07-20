package com.library.model.data.dao;

import com.library.model.data.DBUtils;
import com.library.model.data.entity.User;
import com.library.model.exceptions.DBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementing of BookDao for working with a MySql server
 */
public class MySqlUserDao implements UserDao {

    private static final Logger log = LogManager.getLogger(UserDao.class);

    private Connection connection;

    public MySqlUserDao(Connection connection) {
        this.connection = connection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<User> get(long id) {

        Optional<User> resultOptional = Optional.empty();

        try {
            PreparedStatement getUserStatement = connection
                    .prepareStatement(SqlQueries.GET_USER_QUERY);
            getUserStatement.setLong(1, id);

            ResultSet rs = getUserStatement.executeQuery();

            if (rs.next()) {
                resultOptional = Optional.of(getUserFromResultRow(rs));
            }

            rs.close();

        } catch (SQLException e) {
            String errorText = String.format("Can't get user by id: %s. Cause: %s", id, e.getMessage());
            log.error(errorText);
            throw new DBException(errorText, e);
        }

        return resultOptional;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> getAll() {

        List<User> users = new ArrayList<>();

        try {
            PreparedStatement selectStatement = connection
                    .prepareStatement(SqlQueries.ALL_USERS_QUERY);

            ResultSet rs = selectStatement.executeQuery();

            while (rs.next()) {
                users.add(getUserFromResultRow(rs));
            }

            rs.close();

        } catch (SQLException e) {
            String errorText = "Can't get users list from DB. Cause: " + e.getMessage();
            log.error(errorText);
            throw new DBException(errorText, e);
        }

        return users;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long save(User user) {

        try {
            PreparedStatement insertStatement = connection
                    .prepareStatement(SqlQueries.SAVE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
            insertStatement.setString(1, user.getEmail());
            insertStatement.setString(2, user.getPassword());
            insertStatement.setString(3, user.getPhone());
            insertStatement.setString(4, user.getFirstName());
            insertStatement.setString(5, user.getLastName());
            insertStatement.setLong(6, user.getRoleId());

            insertStatement.executeUpdate();

            return DBUtils.getIdFromStatement(insertStatement);

        } catch (SQLException e) {
            if (DBUtils.isTryingToInsertDuplicate(e)) {
                return -1;
            } else {
                String errorText = String.format("Can't save user: %s. Cause: %s", user, e.getMessage());
                log.error(errorText);
                throw new DBException(errorText, e);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(User user) {

        try {
            PreparedStatement updateStatement = connection
                    .prepareStatement(SqlQueries.UPDATE_USER_INFO_QUERY);
            updateStatement.setString(1, user.getEmail());
            updateStatement.setString(2, user.getPassword());
            updateStatement.setString(3, user.getPhone());
            updateStatement.setString(4, user.getFirstName());
            updateStatement.setString(5, user.getLastName());
            updateStatement.setLong(6, user.getId());

            updateStatement.execute();

        } catch (SQLException e) {
            String errorText = String.format("Can't update user: %s. Cause: %s", user, e.getMessage());
            log.error(errorText);
            throw new DBException(errorText, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(User user) {

        try {
            PreparedStatement deleteStatement = connection
                    .prepareStatement(SqlQueries.DELETE_USER_QUERY);
            deleteStatement.setLong(1, user.getId());

            deleteStatement.execute();

        } catch (SQLException e) {
            String errorText = String.format("Can't delete user: %s. Cause: %s", user, e.getMessage());
            log.error(errorText);
            throw new DBException(errorText, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateKarma(long userId, int delta) {

        try {
            PreparedStatement updateStatement = connection.prepareStatement(SqlQueries.UPDATE_USER_KARMA_QUERY);
            updateStatement.setInt(1, delta);
            updateStatement.setLong(2, userId);

            updateStatement.execute();

        } catch (SQLException e) {
            String errorText = String.format("Can't update user's karma. User id: %s. Cause: %s", userId, e.getMessage());
            log.error(errorText);
            throw new DBException(errorText, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateRole(long userId, long roleId) {

        try {
            PreparedStatement updateStatement = connection.prepareStatement(SqlQueries.UPDATE_USER_ROLE_QUERY);
            updateStatement.setLong(1, roleId);
            updateStatement.setLong(2, userId);

            updateStatement.execute();

        } catch (SQLException e) {
            String errorText = String.format("Can't update user's role_id. User id: %s. Role id: %s. " +
                    "Cause: %s",userId, roleId, e.getMessage());
            log.error(errorText);
            throw new DBException(errorText, e);
        }

    }

    protected User getUserFromResultRow(ResultSet rs) throws SQLException {
         return User.builder()
                .id(rs.getLong("user_id"))
                .email(rs.getString("email"))
                .password(rs.getString("password"))
                .phone(rs.getString("phone"))
                .firstName(rs.getString("first_name"))
                .lastName(rs.getString("last_name"))
                .roleId(rs.getLong("role_id"))
                .karma(rs.getInt("karma"))
                .build();
    }
}
