package com.library.repository.dao.impl;

import com.library.repository.DBUtils;
import com.library.repository.dao.BookDao;
import com.library.repository.entity.Book;
import com.library.repository.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementing of BookDao for working with a MySql server
 */
public class BookDaoImpl implements BookDao {

    private static final Logger log = LogManager.getLogger(BookDaoImpl.class);

    private Connection connection;

    public BookDaoImpl(Connection connection) {
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
                    .prepareStatement(DBQueries.GET_BOOK_QUERY);

            getBookStatement.setLong(1, bookId);

            ResultSet rs = getBookStatement.executeQuery();

            if (rs.next()) {
                resultOptional = Optional.of(getBookFromResultRow(rs));
            }

            rs.close();

        } catch (SQLException e) {
            String errorText = String.format("Can't get book by id: %s. Cause: %s", bookId, e.getMessage());
            log.error(errorText, e);
            throw new DaoException(errorText, e);
        }

        return resultOptional;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Book> getAll() {
        final long EMPTY_ID = -1L;
        return getAllBookParameterized(EMPTY_ID, EMPTY_ID, "", Integer.MAX_VALUE, 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Book> getAllBookParameterized(long authorId, long keywordId,
                                              String partOfTitle, int limit, int offset) {

        List<Book> books = new ArrayList<>();

        try{
            PreparedStatement statement = getPreparedAllBookStatement(authorId, keywordId, partOfTitle,
                    limit, offset, false);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                books.add(getBookFromResultRow(rs));
            }

            rs.close();

        } catch (SQLException e) {
            String errorText = "Can't get books list from DB. Cause: " + e.getMessage();
            log.error(errorText, e);
            throw new DaoException(errorText, e);
        }

        return books;
    }

    /**
     * Gives {@code PreparedStatement} depending on data
     * existence in every of three methods argument
     * @param authorId - the author's ID
     * @param keywordId - the keyword's ID
     * @param partOfTitle - part of book's title or whole title
     * @param limit the number of loans returned
     * @param offset the number of loans returned
     * @param rowsCounting defines type of result {@code Statement}.
     *                     If {@code rowsCounting} is {true} statement
     *                     will return count of all target books.
     *                     It will return only limited count of books otherwise.
     * @return - statement for getting books information from DB
     */
    private PreparedStatement getPreparedAllBookStatement(long authorId, long keywordId, String partOfTitle,
                                                          int limit, int offset, boolean rowsCounting) throws SQLException {

        StringBuilder queryBuilder = new StringBuilder();

        if (rowsCounting) {
            queryBuilder.append(DBQueries.ALL_BOOKS_COUNT_QUERY_HEAD_PART);
        } else {
            queryBuilder.append(DBQueries.ALL_BOOKS_QUERY_HEAD_PART);
        }

        if (authorId > 0) {
            queryBuilder.append(" ").append(DBQueries.ALL_BOOKS_QUERY_AUTHOR_PART);
        }
        if (keywordId > 0) {
            queryBuilder.append(" ").append(DBQueries.ALL_BOOKS_QUERY_KEYWORD_PART);
        }

        if (rowsCounting) {
            queryBuilder.append(" ").append(DBQueries.ALL_BOOKS_COUNT_QUERY_TAIL_PART);
        } else {
            queryBuilder.append(" ").append(DBQueries.ALL_BOOKS_QUERY_TAIL_PART);
        }

        PreparedStatement statement = connection.prepareStatement(queryBuilder.toString());

        int parameterIndex = 1;
        if (authorId > 0) {
            statement.setLong(parameterIndex++, authorId);
        }
        if (keywordId > 0) {
            statement.setLong(parameterIndex++, keywordId);
        }
        statement.setString(parameterIndex++, "%" + partOfTitle + "%");

        if (!rowsCounting) {
            statement.setInt(parameterIndex++, limit);
            statement.setInt(parameterIndex, offset);
        }


        return statement;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getBooSearchResultCount(long authorId, long keywordId, String partOfTitle){
        try{
            PreparedStatement statement = getPreparedAllBookStatement(authorId, keywordId, partOfTitle,
                    Integer.MAX_VALUE, 0, true);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getLong(1);
            } else {
                return 0;
            }

        } catch (SQLException e) {
            String errorText = "Can't get books count in search result. Cause: " + e.getMessage();
            log.error(errorText, e);
            throw new DaoException(errorText, e);
        }
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
                log.error(errorText, e);
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
                    .prepareStatement(DBQueries.UPDATE_BOOK_QUERY);
            updateStatement.setString(1, book.getTitle());
            updateStatement.setInt(2, book.getYear());
            updateStatement.setString(3, book.getDescription());
            updateStatement.setLong(4, book.getId());

            updateStatement.execute();

        } catch (SQLException e) {
            String errorText = String.format("Can't update book: %s. Cause: %s", book, e.getMessage());
            log.error(errorText, e);
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
                    .prepareStatement(DBQueries.DELETE_BOOK_QUERY);
            deleteStatement.setLong(1, book.getId());

            deleteStatement.execute();

        } catch (SQLException e) {
            String errorText = String.format("Can't delete book: %s. Cause: %s", book, e.getMessage());
            log.error(errorText, e);
            throw new DaoException(errorText, e);
        }
    }

    private long insertRowIntoBookTable(Book book) throws SQLException {

        PreparedStatement insertStatement = connection
                .prepareStatement(DBQueries.SAVE_BOOK_QUERY, Statement.RETURN_GENERATED_KEYS);

        insertStatement.setString(1, book.getTitle());
        insertStatement.setInt(2, book.getYear());
        insertStatement.setString(3, book.getDescription());

        insertStatement.executeUpdate();

        return DBUtils.getIdFromStatement(insertStatement);
    }

    Book getBookFromResultRow(ResultSet rs) throws SQLException {
        return Book.builder()
                .id(rs.getLong("book_id"))
                .title(rs.getString("title"))
                .year(rs.getInt("year"))
                .description(rs.getString("description"))
                .build();
    }
}
