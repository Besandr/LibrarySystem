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

    /**
     * Executes command which needs transaction logic.
     * After work closes connection.
     * @param command - command to be executed
     * @return - object returned by command if executing successful
     *           or null if executing failed
     */
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

    /**
     * Executes command which has no need in transaction logic.
     * After work closes connection.
     * @param command - command to be executed
     * @return - object returned by command if executing successful
     *           or null if executing failed
     */
    public Object executeAndClose(DaoManagerCommand command) {

        Object result = null;

        try {
            getConnection();

            result = command.execute(this);

        } catch (SQLException e) {
            log.error("SQLException occurred. Cause: " + e.getMessage());
            return null;
        } catch (DBException e) {
            //SQL query/ies are failed so there is nothing to return
            return null;
        } finally {
            closeConnection();
        }

        return result;
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

    public LoanDtoDao getLoanDtoDao() throws SQLException {
        return new MySqlLoanDtoDao(this, getConnection());
    }

    public interface DaoManagerCommand{
        Object execute(DaoManager manager) throws SQLException;
    }

}
