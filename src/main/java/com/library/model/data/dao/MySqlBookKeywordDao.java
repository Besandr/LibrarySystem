package com.library.model.data.dao;

import com.library.model.data.entity.Book;
import com.library.model.data.entity.Keyword;
import com.library.model.exceptions.DBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;

/**
 * Implementing of BookKeywordDao for working with a MySql server
 */
public class MySqlBookKeywordDao implements BookKeywordDao {

    private final static Logger log = LogManager.getLogger(MySqlBookKeywordDao.class);

    private Connection connection;

    public MySqlBookKeywordDao(Connection connection) {
        this.connection = connection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveBookKeywordsJunction(Book book, Set<Keyword> keywords) {

        try {
            PreparedStatement statement = connection.prepareStatement(SqlQueries.INSERT_BOOK_KEYWORD_QUERY);

            for (Keyword keyword : keywords) {
                statement.setLong(1, book.getId());
                statement.setLong(2, keyword.getId());
                statement.addBatch();
            }

            statement.execute();
        } catch (SQLException e) {
            String errorText = String.format("Can't add rows to book_keyword table. Book: %s. Cause: %s", book, e.getMessage());
            log.error(errorText);
            throw new DBException(errorText, e);
        }
    }
}
