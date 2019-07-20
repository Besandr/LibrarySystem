package com.library.model.data.dao;

import com.library.model.data.entity.Author;
import com.library.model.data.entity.Book;
import com.library.model.exceptions.DBException;
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
public class MySqlAuthorBookDao implements AuthorBookDao{

    private final static Logger log = LogManager.getLogger(MySqlAuthorBookDao.class);

    private Connection connection;

    public MySqlAuthorBookDao(Connection connection) {
        this.connection = connection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveAuthorBookJunction(Book book, Set<Author> authors) {

        try {
            PreparedStatement statement = connection.prepareStatement(SqlQueries.INSERT_AUTHOR_BOOK_QUERY);

            for (Author author : authors) {
                statement.setLong(1, author.getId());
                statement.setLong(2, book.getId());
                statement.addBatch();
            }

            statement.execute();
        } catch (SQLException e) {
            String errorText = String.format("Can't add rows to author_book table. Book: %s. Cause: %s", book, e.getMessage());
            log.error(errorText);
            throw new DBException(errorText, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAuthorBookJunction(Author author, Book book) {
        try{
            PreparedStatement statement = connection.prepareStatement(SqlQueries.DELETE_AUTHOR_BOOK_QUERY);
            statement.setLong(1, author.getId());
            statement.setLong(2, book.getId());

            statement.execute();

        } catch (SQLException e) {
            String errorText = String.format("Can't delete row from author_book. Author: %s." +
                    "Book: %s. Cause: %s", author, book, e.getMessage());
            log.error(errorText);
            throw new DBException(errorText, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean doesAuthorHasBooks(Author author) {

        try{
            PreparedStatement statement = connection.prepareStatement(SqlQueries.COUNT_BOOKS_OF_AUTHOR_QUERY);
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
            log.error(errorText);
            throw new DBException(errorText, e);
        }
    }
}
