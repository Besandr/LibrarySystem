package com.library.repository;

import com.library.repository.dao.*;
import com.library.repository.dao.impl.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Manages connections lifecycle, transactions and DAO creations
 */
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

            } catch (DaoException e) {
                connection.rollback();
            }
            return result;

        } catch (SQLException e) {
            log.error("SQLException occurred. Cause: " + e.getMessage(), e);
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

        try {
            getConnection();

            return command.execute(this);

        } catch (SQLException e) {
            log.error("SQLException occurred. Cause: " + e.getMessage(), e);
            return null;
        } catch (DaoException e) {
            //Command executing is failed so there is nothing to return
            return null;
        } finally {
            closeConnection();
        }
    }

    /**
     * Closes manager's connection and handles a possible Exception
     */
    private void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            log.error("Can't close connection. Cause: " + e.getMessage(), e);
        }
    }

    protected Connection getConnection() throws SQLException {
        if (connection == null) {
            connection = dataSource.getConnection();
        }
        return connection;
    }

    // Dao getters
    public AuthorDao getAuthorDao() throws SQLException {
        return new AuthorDaoImpl(getConnection());
    }

    public BookDao getBookDao() throws SQLException {
        return new BookDaoImpl(getConnection());
    }

    public AuthorBookDao getAuthorBookDao() throws SQLException {
        return new AuthorBookDaoImpl(getConnection());
    }

    public KeywordDao getKeywordDao() throws SQLException {
        return new KeywordDaoImpl(getConnection());
    }

    public BookKeywordDao getBookKeywordDao() throws SQLException {
        return new BookKeywordDaoImpl(getConnection());
    }

    public LocationDao getLocationDao() throws SQLException {
        return new LocationDaoImpl(getConnection());
    }

    public LoanDao getLoanDao() throws SQLException {
        return new LoanDaoImpl(getConnection());
    }

    public BookcaseDao getBookcaseDao() throws SQLException {
        return new BookcaseDaoImpl(getConnection());
    }

    public UserDao getUserDao() throws SQLException {
        return new UserDaoImpl(getConnection());
    }

    public LoanDtoDao getLoanDtoDao() throws SQLException {
        return new LoanDtoDaoImpl(this, getConnection());
    }

    /**
     * Command class for executing by DaoManager
     */
    @FunctionalInterface
    public interface DaoManagerCommand{
        Object execute(DaoManager manager) throws SQLException;
    }

}
