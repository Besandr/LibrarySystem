package com.library.model.data.dao;

import com.library.model.data.DBUtils;
import com.library.model.data.entity.Author;
import com.library.model.data.entity.Book;
import com.library.model.data.entity.Keyword;
import com.library.model.exceptions.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementing of BookDao for working with a MySql server
 */
public class MySqlBookDao implements BookDao {

    private static final Logger log = LogManager.getLogger(MySqlBookDao.class);

    private Connection connection;

    public MySqlBookDao(Connection connection) {
        this.connection = connection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Book> get(long bookId) {

        Optional<Book> resultOptional = Optional.empty();

        try {

            PreparedStatement getBookStatement = connection
                    .prepareStatement(SqlQueries.GET_BOOK_QUERY);

            getBookStatement.setLong(1, bookId);

            ResultSet rs = getBookStatement.executeQuery();

            if (rs.next()) {
                resultOptional = Optional.of(getBookFromResultRow(rs));
            }

            rs.close();

        } catch (SQLException e) {
            String errorText = String.format("Can't get book by id: %s. Cause: %s", bookId, e.getMessage());
            log.error(errorText);
            throw new DaoException(errorText, e);
        }

        return resultOptional;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Book> getAll() {
        return getAllBookParameterized(Optional.empty(), Optional.empty(), "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Book> getAllBookParameterized(Optional<Author> author, Optional<Keyword> keyword, String partOfTitle) {

        List<Book> books = new ArrayList<>();

        try{
            PreparedStatement statement = getPreparedStatement(author, keyword, partOfTitle);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                books.add(getBookFromResultRow(rs));
            }

            rs.close();

        } catch (SQLException e) {
            String errorText = "Can't get books list from DB. Cause: " + e.getMessage();
            log.error(errorText);
            throw new DaoException(errorText, e);
        }

        return books;
    }

    protected PreparedStatement getPreparedStatement(Optional<Author> author, Optional<Keyword> keyword, String partOfTitle) throws SQLException {

        StringBuilder queryBuilder = new StringBuilder(SqlQueries.ALL_BOOKS_QUERY_HEAD_PART);
        if (author.isPresent()) {
            queryBuilder.append(" ").append(SqlQueries.ALL_BOOKS_QUERY_AUTHOR_PART);
        }
        if (keyword.isPresent()) {
            queryBuilder.append(" ").append(SqlQueries.ALL_BOOKS_QUERY_KEYWORD_PART);
        }
        queryBuilder.append(" ").append(SqlQueries.ALL_BOOKS_QUERY_TAIL_PART);

        PreparedStatement statement = connection.prepareStatement(queryBuilder.toString());

        int parameterIndex = 1;
        if (author.isPresent()) {
            statement.setLong(parameterIndex++, author.get().getId());
        }
        if (keyword.isPresent()) {
            statement.setLong(parameterIndex++, keyword.get().getId());
        }
        statement.setString(parameterIndex, "%" + partOfTitle + "%");

        return statement;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long save(Book book) {

        try {

            return insertRowIntoBookTable(book);

        } catch (SQLException e) {
            if (DBUtils.isTryingToInsertDuplicate(e)) {
                return -1;
            } else {
                String errorText = String.format("Can't save book: %s. Cause: %s", book, e.getMessage());
                log.error(errorText);
                throw new DaoException(errorText, e);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Book book) {

        try {
            PreparedStatement updateStatement = connection
                    .prepareStatement(SqlQueries.UPDATE_BOOK_QUERY);
            updateStatement.setString(1, book.getTitle());
            updateStatement.setInt(2, book.getYear());
            updateStatement.setString(3, book.getDescription());
            updateStatement.setLong(4, book.getId());

            updateStatement.execute();

        } catch (SQLException e) {
            String errorText = String.format("Can't update book: %s. Cause: %s", book, e.getMessage());
            log.error(errorText);
            throw new DaoException(errorText, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Book book) {

        try {
            PreparedStatement deleteStatement = connection
                    .prepareStatement(SqlQueries.DELETE_BOOK_QUERY);
            deleteStatement.setLong(1, book.getId());

            deleteStatement.execute();

        } catch (SQLException e) {
            String errorText = String.format("Can't delete book: %s. Cause: %s", book, e.getMessage());
            log.error(errorText);
            throw new DaoException(errorText, e);
        }
    }

    protected long insertRowIntoBookTable(Book book) throws SQLException {

        PreparedStatement insertStatement = connection
                .prepareStatement(SqlQueries.SAVE_BOOK_QUERY, Statement.RETURN_GENERATED_KEYS);

        insertStatement.setString(1, book.getTitle());
        insertStatement.setInt(2, book.getYear());
        insertStatement.setString(3, book.getDescription());

        insertStatement.executeUpdate();

        return DBUtils.getIdFromStatement(insertStatement);
    }

    protected Book getBookFromResultRow(ResultSet rs) throws SQLException {
        Book book = Book.builder()
                .id(rs.getLong("book_id"))
                .title(rs.getString("title"))
                .year(rs.getInt("year"))
                .description(rs.getString("description"))
                .build();
        return book;
    }
}
