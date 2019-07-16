package com.library.model.data.dao;

import com.library.model.data.DBUtils;
import com.library.model.data.entity.Role;
import com.library.model.exceptions.DBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MySqlRoleDao implements RoleDao {

    private static final Logger log = LogManager.getLogger(RoleDao.class);

    private Connection connection;

    public MySqlRoleDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Role> get(long id) {

        Optional<Role> resultOptional = Optional.empty();

        try {
            PreparedStatement getRoleStatement = connection
                    .prepareStatement(SqlQueries.GET_ROLE_QUERY);
            getRoleStatement.setLong(1, id);

            ResultSet rs = getRoleStatement.executeQuery();

            if (rs.next()) {
                resultOptional = Optional.of(getRoleFromResultRow(rs));
            }

            rs.close();

        } catch (SQLException e) {
            String errorText = String.format("Can't get role by id: %s. Exception message: %s", id, e.getMessage());
            log.error(errorText);
            throw new DBException(errorText, e);
        }

        return resultOptional;
    }

    @Override
    public List<Role> getAll() {

        List<Role> roles = new ArrayList<>();

        try {
            PreparedStatement selectStatement = connection
                    .prepareStatement(SqlQueries.ALL_ROLES_QUERY);
            ResultSet rs = selectStatement.executeQuery();

            while (rs.next()) {
                roles.add(getRoleFromResultRow(rs));
            }

            rs.close();

        } catch (SQLException e) {
            String errorText = "Can't get roles list from DB. Exception message: " + e.getMessage();
            log.error(errorText);
            throw new DBException(errorText, e);
        }

        return roles;
    }

    @Override
    public long save(Role role) {

        long id;

        try {
            PreparedStatement insertStatement = connection
                    .prepareStatement(SqlQueries.SAVE_ROLE_QUERY, Statement.RETURN_GENERATED_KEYS);

            insertStatement.setString(1, role.getName());

            insertStatement.executeUpdate();

            id = DBUtils.getIdFromStatement(insertStatement);

        } catch (SQLException e) {
            String errorText = String.format("Can't save role: %s. Exception message: %s", role, e.getMessage());
            log.error(errorText);
            throw new DBException(errorText, e);
        }
        return id;
    }

    @Override
    public void update(Role role) {

        try {
            PreparedStatement updateStatement = connection
                    .prepareStatement(SqlQueries.UPDATE_ROLE_QUERY);
            updateStatement.setString(1, role.getName());
            updateStatement.setLong(2, role.getId());

            updateStatement.execute();

        } catch (SQLException e) {
            String errorText = String.format("Can't update role: %s. Exception message: %s", role, e.getMessage());
            log.error(errorText);
            throw new DBException(errorText, e);
        }
    }

    @Override
    public void delete(Role role) {

        try {
            PreparedStatement deleteStatement = connection
                    .prepareStatement(SqlQueries.DELETE_ROLE_QUERY);
            deleteStatement.setLong(1, role.getId());

            deleteStatement.execute();

        } catch (SQLException e) {
            String errorText = String.format("Can't delete role: %s. Exception message: %s", role, e.getMessage());
            log.error(errorText);
            throw new DBException(errorText, e);
        }
    }

    protected Role getRoleFromResultRow(ResultSet rs) throws SQLException {
        return Role.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .build();
    }
}
