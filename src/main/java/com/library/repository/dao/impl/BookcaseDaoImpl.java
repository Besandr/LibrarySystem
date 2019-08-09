package com.library.repository.dao.impl;

import com.library.repository.DBUtils;
import com.library.repository.dao.BookcaseDao;
import com.library.repository.entity.Bookcase;
import com.library.repository.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementing of BookcaseDao for working with a SQL server
 */
public class BookcaseDaoImpl implements BookcaseDao {

    private static final Logger log = LogManager.getLogger(BookcaseDao.class);

    private Connection connection;

    public BookcaseDaoImpl(Connection connection) {
        this.connection = connection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Bookcase> get(long id) {

        Optional<Bookcase> resultOptional = Optional.empty();

        try {
            PreparedStatement getBookcaseStatement = connection
                    .prepareStatement(DBQueries.GET_BOOKCASE_QUERY);
            getBookcaseStatement.setLong(1, id);

            ResultSet rs = getBookcaseStatement.executeQuery();

            if (rs.next()) {
                resultOptional = Optional.of(getBookcaseFromResultRow(rs));
            }

            rs.close();

        } catch (SQLException e) {
            String errorText = String.format("Can't get bookcase by id: %s. Cause: %s", id, e.getMessage());
            log.error(errorText, e);
            throw new DaoException(errorText, e);
        }

        return resultOptional;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Bookcase> getAll() {

        List<Bookcase> bookcases = new ArrayList<>();

        try {
            PreparedStatement selectStatement = connection
                    .prepareStatement(DBQueries.ALL_BOOKCASES_QUERY);

            ResultSet rs = selectStatement.executeQuery();

            while (rs.next()) {
                bookcases.add(getBookcaseFromResultRow(rs));
            }

            rs.close();

        } catch (SQLException e) {
            String errorText = "Can't get bookcases list from DB. Cause: " + e.getMessage();
            log.error(errorText, e);
            throw new DaoException(errorText, e);
        }
        return bookcases;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long save(Bookcase bookcase) {

        try {
            PreparedStatement insertStatement = connection
                    .prepareStatement(DBQueries.SAVE_BOOKCASE_QUERY, Statement.RETURN_GENERATED_KEYS);
            insertStatement.setInt(1, bookcase.getShelfQuantity());
            insertStatement.setInt(2, bookcase.getCellQuantity());

            insertStatement.executeUpdate();

            return DBUtils.getIdFromStatement(insertStatement);

        } catch (SQLException e) {
            if (DBUtils.isTryingToInsertDuplicate(e)) {
                return -1;
            } else {
                String errorText = String.format("Can't save bookcase: %s. Cause: %s", bookcase, e.getMessage());
                log.error(errorText, e);
                throw new DaoException(errorText, e);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Bookcase bookcase) {

        try {
            PreparedStatement updateStatement = connection
                    .prepareStatement(DBQueries.UPDATE_BOOKCASE_QUERY);
            updateStatement.setInt(1, bookcase.getShelfQuantity());
            updateStatement.setInt(2, bookcase.getCellQuantity());
            updateStatement.setLong(3, bookcase.getId());

            updateStatement.execute();

        } catch (SQLException e) {
            String errorText = String.format("Can't update bookcase: %s. Cause: %s", bookcase, e.getMessage());
            log.error(errorText, e);
            throw new DaoException(errorText, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Bookcase bookcase) {

        try {
            PreparedStatement deleteStatement = connection
                    .prepareStatement(DBQueries.DELETE_BOOKCASE_QUERY);
            deleteStatement.setLong(1, bookcase.getId());

            deleteStatement.execute();

        } catch (SQLException e) {
            String errorText = String.format("Can't delete bookcase: %s. Cause: %s", bookcase, e.getMessage());
            log.error(errorText, e);
            throw new DaoException(errorText, e);
        }
    }

    private Bookcase getBookcaseFromResultRow(ResultSet rs) throws SQLException {

        return Bookcase.builder()
                .id(rs.getLong("bookcase_id"))
                .shelfQuantity(rs.getInt("shelf_quantity"))
                .cellQuantity(rs.getInt("cell_quantity"))
                .build();
    }
}
