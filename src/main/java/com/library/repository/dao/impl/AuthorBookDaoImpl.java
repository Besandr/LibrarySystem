package com.library.repository.dao.impl;

import com.library.repository.dao.AuthorBookDao;
import com.library.repository.entity.Author;
import com.library.repository.entity.Book;
import com.library.repository.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

/**
 * Implementing of AuthorBookDao for working with a MySql server
 */
public class AuthorBookDaoImpl implements AuthorBookDao {

    private final static Logger log = LogManager.getLogger(AuthorBookDaoImpl.class);

    private Connection connection;

    public AuthorBookDaoImpl(Connection connection) {
        this.connection = connection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveAuthorBookJunction(Book book, Set<Author> authors) {

        try {
            PreparedStatement statement = connection.prepareStatement(DBQueries.INSERT_AUTHOR_BOOK_QUERY);

            for (Author author : authors) {
                statement.setLong(1, author.getId());
                statement.setLong(2, book.getId());
                statement.addBatch();
            }

            statement.execute();
        } catch (SQLException e) {
            String errorText = String.format("Can't add rows to author_book table. Book: %s. Cause: %s", book, e.getMessage());
            log.error(errorText, e);
            throw new DaoException(errorText, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAuthorBookJunction(Author author, Book book) {
        try{
            PreparedStatement statement = connection.prepareStatement(DBQueries.DELETE_AUTHOR_BOOK_QUERY);
            statement.setLong(1, author.getId());
            statement.setLong(2, book.getId());

            statement.execute();

        } catch (SQLException e) {
            String errorText = String.format("Can't delete row from author_book. Author: %s." +
                    "Book: %s. Cause: %s", author, book, e.getMessage());
            log.error(errorText, e);
            throw new DaoException(errorText, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean doesAuthorHaveBooks(Author author) {

        try{
            PreparedStatement statement = connection.prepareStatement(DBQueries.COUNT_BOOKS_OF_AUTHOR_QUERY);
            statement.setLong(1, author.getId());
            //Result set will contain only one string with given author books quantity
            ResultSet rs = statement.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            String errorText = String.format("Can't count books of author. Author: %s. Cause: %s", author, e.getMessage());
            log.error(errorText, e);
            throw new DaoException(errorText, e);
        }
    }
}
