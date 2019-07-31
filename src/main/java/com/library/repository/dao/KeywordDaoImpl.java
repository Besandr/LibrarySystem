package com.library.repository.dao;

import com.library.repository.DBUtils;
import com.library.repository.entity.Keyword;
import com.library.repository.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementing of KeywordDao for working with a MySql server
 */
public class KeywordDaoImpl implements KeywordDao{

    private static final Logger log = LogManager.getLogger(KeywordDao.class);

    private Connection connection;

    public KeywordDaoImpl(Connection connection) {
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
                    .prepareStatement(DBQueries.GET_KEYWORD_QUERY);
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
                    .prepareStatement(DBQueries.GET_KEYWORD_BY_WORD_QUERY);
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
                    .prepareStatement(DBQueries.ALL_KEYWORDS_QUERY);

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
    public List<Keyword> getByBookId(long bookId) {
        List<Keyword> keywords = new ArrayList<>();

        try {
            PreparedStatement selectStatement = connection
                    .prepareStatement(DBQueries.GET_KEYWORDS_BY_BOOK_QUERY);
            selectStatement.setLong(1, bookId);

            ResultSet rs = selectStatement.executeQuery();

            while (rs.next()) {
                keywords.add(getKeywordFromResultRow(rs));
            }

            rs.close();

        } catch (SQLException e) {
            String errorText = String.format("Can't get keywords list by Book from DB. Book: %s. Cause: %s", bookId, e.getMessage());
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
                    .prepareStatement(DBQueries.SAVE_KEYWORD_QUERY, Statement.RETURN_GENERATED_KEYS);
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
                    .prepareStatement(DBQueries.UPDATE_KEYWORD_QUERY);
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
                    .prepareStatement(DBQueries.DELETE_KEYWORD_QUERY);
            deleteStatement.setLong(1, keyword.getId());

            deleteStatement.execute();

        } catch (SQLException e) {
            String errorText = String.format("Can't delete keyword: %s. Cause: %s", keyword, e.getMessage());
            log.error(errorText);
            throw new DaoException(errorText, e);
        }
    }

    private Keyword getKeywordFromResultRow(ResultSet rs) throws SQLException {
        return Keyword.builder()
                .id(rs.getLong("keyword_id"))
                .word(rs.getString("word"))
                .build();
    }
}
