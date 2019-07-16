package com.library.model.data;

import com.library.model.data.dao.*;
import com.library.model.exceptions.DBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DaoManager {

    private static final Logger log = LogManager.getLogger(DaoManager.class);

    private DataSource dataSource;

    private Connection connection;

    public DaoManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Object executeTransaction(DaoManagerCommand command) {

        Object result = null;

        try {
            getConnection();
            connection.setAutoCommit(false);

            try {

                result = command.execute(this);
                connection.commit();

            } catch (DBException e) {
                connection.rollback();
            }

            return result;

        } catch (SQLException e) {
            log.error("SQLException occurred. Cause: " + e.getMessage());
            return null;
        } finally {
            closeConnection();
        }
    }

    private void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            log.error("Can't close connection. Cause: " + e.getMessage());
        }
    }

    protected Connection getConnection() throws SQLException {
        if (connection == null) {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
        }
        return connection;
    }

    public Dao getAuthorDao() throws SQLException {
        return new MySqlAuthorDao(getConnection());
    }

    public Dao getBookDao() throws SQLException {
        return new MySqlBookDao(getConnection());
    }

    public Dao getLocationDao() throws SQLException {
        return new MySqlLocationDao(getConnection());
    }

    public Dao getLoanDao() throws SQLException {
        return new MySqlLoanDao(getConnection());
    }

    public Dao getKeywordDao() throws SQLException {
        return new MySqlKeywordDao(getConnection());
    }

    public Dao getBookcaseDao() throws SQLException {
        return new MySqlBookcaseDao(getConnection());
    }

    public Dao getUserDao() throws SQLException {
        return new MySqlUserDao(getConnection());
    }

/*    public Dao getDao(Class clazz) throws SQLException {

        switch (clazz.getSimpleName()) {
            case "MySqlAuthorDao":
                return new MySqlAuthorDao(getConnection());
            case "MySqlBookDao":
                return new MySqlBookDao(getConnection());
            case "MySqlLocationDao":
                return new MySqlLocationDao(getConnection());
            case "MySqlLoanDao":
                return new MySqlLoanDao(getConnection());
            case "MySqlKeywordDao":
                return new MySqlKeywordDao(getConnection());
        }
        return null;
    }*/

    public interface DaoManagerCommand{
        Object execute(DaoManager manager) throws SQLException;
    }

}
