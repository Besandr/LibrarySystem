package com.library.model.data.dao;

import com.library.model.data.DBUtils;
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
 * Implementing of KeywordDao for working with a MySql server
 */
public class MySqlKeywordDao implements KeywordDao{

    private static final Logger log = LogManager.getLogger(KeywordDao.class);

    private Connection connection;

    public MySqlKeywordDao(Connection connection) {
        this.connection = connection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Keyword> get(long id) {

        Optional<Keyword> resultOptional = Optional.empty();

        try {
            PreparedStatement getKeywordStatement = connection
                    .prepareStatement(MySqlQueries.GET_KEYWORD_QUERY);
            getKeywordStatement.setLong(1, id);

            ResultSet rs = getKeywordStatement.executeQuery();

            if (rs.next()) {
                resultOptional = Optional.of(getKeywordFromResultRow(rs));
            }

            rs.close();

        } catch (SQLException e) {
            String errorText = String.format("Can't get keyword by id: %s. Cause: %s", id, e.getMessage());
            log.error(errorText);
            throw new DaoException(errorText, e);
        }

        return resultOptional;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Keyword> getByWord(String word) {

        Optional<Keyword> resultOptional = Optional.empty();

        try {
            PreparedStatement getKeywordStatement = connection
                    .prepareStatement(MySqlQueries.GET_KEYWORD_BY_WORD_QUERY);
            getKeywordStatement.setString(1, word);

            ResultSet rs = getKeywordStatement.executeQuery();

            if (rs.next()) {
                resultOptional = Optional.of(getKeywordFromResultRow(rs));
            }

            rs.close();

        } catch (SQLException e) {
            String errorText = String.format("Can't get keyword by word: %s. Cause: %s", word, e.getMessage());
            log.error(errorText);
            throw new DaoException(errorText, e);
        }

        return resultOptional;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Keyword> getAll() {

        List<Keyword> keywords = new ArrayList<>();

        try {
            PreparedStatement selectStatement = connection
                    .prepareStatement(MySqlQueries.ALL_KEYWORDS_QUERY);

            ResultSet rs = selectStatement.executeQuery();

            while (rs.next()) {
                keywords.add(getKeywordFromResultRow(rs));
            }

            rs.close();

        } catch (SQLException e) {
            String errorText = "Can't get keywords list from DB. Cause: " + e.getMessage();
            log.error(errorText);
            throw new DaoException(errorText, e);
        }

        return keywords;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Keyword> getByBook(Book book) {
        List<Keyword> keywords = new ArrayList<>();

        try {
            PreparedStatement selectStatement = connection
                    .prepareStatement(MySqlQueries.GET_KEYWORDS_BY_BOOK_QUERY);
            selectStatement.setLong(1, book.getId());

            ResultSet rs = selectStatement.executeQuery();

            while (rs.next()) {
                keywords.add(getKeywordFromResultRow(rs));
            }

            rs.close();

        } catch (SQLException e) {
            String errorText = String.format("Can't get keywords list by Book from DB. Book: %s. Cause: %s", book, e.getMessage());
            log.error(errorText);
            throw new DaoException(errorText, e);
        }

        return keywords;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long save(Keyword keyword) {

        try {
            PreparedStatement insertStatement = connection
                    .prepareStatement(MySqlQueries.SAVE_KEYWORD_QUERY, Statement.RETURN_GENERATED_KEYS);
            insertStatement.setString(1, keyword.getWord());

            insertStatement.executeUpdate();

            return DBUtils.getIdFromStatement(insertStatement);

        } catch (SQLException e) {
            if (DBUtils.isTryingToInsertDuplicate(e)) {
                return -1;
            } else {
                String errorText = String.format("Can't save keyword: %s. Cause: %s", keyword, e.getMessage());
                log.error(errorText);
                throw new DaoException(errorText, e);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Keyword keyword) {

        try {
            PreparedStatement updateStatement = connection
                    .prepareStatement(MySqlQueries.UPDATE_KEYWORD_QUERY);
            updateStatement.setString(1, keyword.getWord());
            updateStatement.setLong(2, keyword.getId());

            updateStatement.execute();

        } catch (SQLException e) {
            String errorText = String.format("Can't update keyword: %s. Cause: %s", keyword, e.getMessage());
            log.error(errorText);
            throw new DaoException(errorText, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Keyword keyword) {

        try {
            PreparedStatement deleteStatement = connection
                    .prepareStatement(MySqlQueries.DELETE_KEYWORD_QUERY);
            deleteStatement.setLong(1, keyword.getId());

            deleteStatement.execute();

        } catch (SQLException e) {
            String errorText = String.format("Can't delete keyword: %s. Cause: %s", keyword, e.getMessage());
            log.error(errorText);
            throw new DaoException(errorText, e);
        }
    }

    protected Keyword getKeywordFromResultRow(ResultSet rs) throws SQLException {
        return Keyword.builder()
                .id(rs.getLong("keyword_id"))
                .word(rs.getString("word"))
                .build();
    }
}
