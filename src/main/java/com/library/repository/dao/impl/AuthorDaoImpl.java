package com.library.repository.dao.impl;

import com.library.repository.DBUtils;
import com.library.repository.dao.AuthorDao;
import com.library.repository.entity.Author;
import com.library.repository.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementing of AuthorDao for working with a MySql server
 */
public class AuthorDaoImpl implements AuthorDao {

    private static final Logger log = LogManager.getLogger(AuthorDaoImpl.class);

    private Connection connection;

    public AuthorDaoImpl(Connection connection) {
        this.connection = connection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Author> get(long id) {

        Optional<Author> resultOptional = Optional.empty();

        try {
            PreparedStatement getAuthorStatement = connection
                    .prepareStatement(DBQueries.GET_AUTHOR_QUERY);
            getAuthorStatement.setLong(1, id);

            ResultSet rs = getAuthorStatement.executeQuery();

            if (rs.next()) {
                resultOptional = Optional.of(getAuthorFromResultRow(rs));
            }

            rs.close();

        } catch (SQLException e) {
            String errorText = String.format("Can't get author by id: %s. Cause: %s", id, e.getMessage());
            log.error(errorText, e);
            throw new DaoException(errorText, e);
        }

        return resultOptional;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Author> getByName(String firstName, String lastName) {

        Optional<Author> resultOptional = Optional.empty();

        try {
            PreparedStatement getAuthorStatement = connection
                    .prepareStatement(DBQueries.GET_AUTHOR_BY_NAME_QUERY);
            getAuthorStatement.setString(1, firstName);
            getAuthorStatement.setString(2, lastName);

            ResultSet rs = getAuthorStatement.executeQuery();

            if (rs.next()) {
                resultOptional = Optional.of(getAuthorFromResultRow(rs));
            }

            rs.close();

        } catch (SQLException e) {
            String errorText = String.format("Can't get author by name: %s %s. Cause: %s", firstName, lastName, e.getMessage());
            log.error(errorText, e);
            throw new DaoException(errorText, e);
        }

        return resultOptional;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Author> getAll() {

        List<Author> authors = new ArrayList<>();

        try {
            PreparedStatement selectStatement = connection
                    .prepareStatement(DBQueries.ALL_AUTHORS_QUERY);

            ResultSet rs = selectStatement.executeQuery();

            while (rs.next()) {
                authors.add(getAuthorFromResultRow(rs));
            }

            rs.close();

        } catch (SQLException e) {
            String errorText = "Can't get authors list from DB. Cause: " + e.getMessage();
            log.error(errorText, e);
            throw new DaoException(errorText, e);
        }

        return authors;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Author> getByBookId(long bookId) {

        List<Author> authors = new ArrayList<>();

        try {
            PreparedStatement selectStatement = connection
                    .prepareStatement(DBQueries.GET_AUTHORS_BY_BOOK_QUERY);
            selectStatement.setLong(1, bookId);

            ResultSet rs = selectStatement.executeQuery();

            while (rs.next()) {
                authors.add(getAuthorFromResultRow(rs));
            }

            rs.close();

        } catch (SQLException e) {
            String errorText = String.format("Can't get authors list by Book from DB. Book: %s. Cause: %s", bookId, e.getMessage());
            log.error(errorText, e);
            throw new DaoException(errorText, e);
        }

        return authors;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long save(Author author) {

        try {
            PreparedStatement insertStatement = connection
                    .prepareStatement(DBQueries.SAVE_AUTHOR_QUERY, Statement.RETURN_GENERATED_KEYS);

            insertStatement.setString(1, author.getFirstName());
            insertStatement.setString(2, author.getLastName());

            insertStatement.executeUpdate();

            return DBUtils.getIdFromStatement(insertStatement);

        } catch (SQLException e) {
            if (DBUtils.isTryingToInsertDuplicate(e)) {
                return -1;
            } else {
                String errorText = String.format("Can't save author: %s. Cause: %s", author, e.getMessage());
                log.error(errorText, e);
                throw new DaoException(errorText, e);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Author author) {

        try {
            PreparedStatement updateStatement = connection
                    .prepareStatement(DBQueries.UPDATE_AUTHOR_QUERY);
            updateStatement.setString(1, author.getFirstName());
            updateStatement.setString(2, author.getLastName());
            updateStatement.setLong(3, author.getId());

            updateStatement.execute();

        } catch (SQLException e) {
            String errorText = String.format("Can't update author: %s. Cause: %s", author, e.getMessage());
            log.error(errorText, e);
            throw new DaoException(errorText, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Author author) {

        try {
            PreparedStatement deleteStatement = connection
                    .prepareStatement(DBQueries.DELETE_AUTHOR_QUERY);
            deleteStatement.setLong(1, author.getId());

            deleteStatement.execute();

        } catch (SQLException e) {
            String errorText = String.format("Can't delete author: %s. Cause: %s", author, e.getMessage());
            log.error(errorText, e);
            throw new DaoException(errorText, e);
        }
    }

    private Author getAuthorFromResultRow(ResultSet rs) throws SQLException {

        return Author.builder()
                .id(rs.getLong("author_id"))
                .firstName(rs.getString("first_name"))
                .lastName(rs.getString("last_name"))
                .build();
    }
}
