package com.library.repository.dao;

import com.library.repository.entity.Book;
import com.library.repository.entity.Keyword;
import com.library.repository.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

/**
 * Implementing of BookKeywordDao for working with a MySql server
 */
public class BookKeywordDaoImpl implements BookKeywordDao {

    private final static Logger log = LogManager.getLogger(BookKeywordDaoImpl.class);

    private Connection connection;

    public BookKeywordDaoImpl(Connection connection) {
        this.connection = connection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveBookKeywordsJunction(Book book, Set<Keyword> keywords) {

        try {
            PreparedStatement statement = connection.prepareStatement(DBQueries.INSERT_BOOK_KEYWORD_QUERY);

            for (Keyword keyword : keywords) {
                statement.setLong(1, book.getId());
                statement.setLong(2, keyword.getId());
                statement.addBatch();
            }

            statement.execute();
        } catch (SQLException e) {
            String errorText = String.format("Can't add rows to book_keyword table. Book: %s. Cause: %s", book, e.getMessage());
            log.error(errorText);
            throw new DaoException(errorText, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean doesKeywordBelongToBook(Keyword keyword) {

        try{
            PreparedStatement statement = connection.prepareStatement(DBQueries.COUNT_BOOKS_WITH_KEYWORD_QUERY);
            statement.setLong(1, keyword.getId());
            //Result set will contain only one string with given author books quantity
            ResultSet rs = statement.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            String errorText = String.format("Can't count books with keyword. Keyword: %s. Cause: %s", keyword, e.getMessage());
            log.error(errorText);
            throw new DaoException(errorText, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteBookKeywordJunction(Keyword keyword, Book book) {

        try{
            PreparedStatement statement = connection.prepareStatement(DBQueries.DELETE_BOOK_KEYWORD_QUERY);
            statement.setLong(1, book.getId());
            statement.setLong(2, keyword.getId());

            statement.execute();

        } catch (SQLException e) {
            String errorText = String.format("Can't delete row from book_keyword. Keyword: %s." +
                    "Book: %s. Cause: %s", keyword, book, e.getMessage());
            log.error(errorText);
            throw new DaoException(errorText, e);
        }
    }
}
