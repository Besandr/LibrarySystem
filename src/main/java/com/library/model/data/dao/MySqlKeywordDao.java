package com.library.model.data.dao;

import com.library.model.data.DBUtils;
import com.library.model.data.entity.Keyword;
import com.library.model.exceptions.DBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MySqlKeywordDao implements KeywordDao{

    private static final Logger log = LogManager.getLogger(KeywordDao.class);

    private Connection connection;

    public MySqlKeywordDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Keyword> get(long id) {

        Optional<Keyword> resultOptional = Optional.empty();

        try {
            PreparedStatement getKeywordStatement = connection
                    .prepareStatement(SqlQueries.GET_KEYWORD_QUERY);
            getKeywordStatement.setLong(1, id);

            ResultSet rs = getKeywordStatement.executeQuery();

            if (rs.next()) {
                resultOptional = Optional.of(getKeywordFromResultRow(rs));
            }

            rs.close();

        } catch (SQLException e) {
            String errorText = String.format("Can't get keyword by id: %s. Exception message: %s", id, e.getMessage());
            log.error(errorText);
            throw new DBException(errorText, e);
        }

        return resultOptional;
    }

    @Override
    public List<Keyword> getAll() {

        List<Keyword> keywords = new ArrayList<>();

        try {
            PreparedStatement selectStatement = connection
                    .prepareStatement(SqlQueries.ALL_KEYWORDS_QUERY);

            ResultSet rs = selectStatement.executeQuery();

            while (rs.next()) {
                keywords.add(getKeywordFromResultRow(rs));
            }

            rs.close();

        } catch (SQLException e) {
            String errorText = "Can't get keywords list from DB. Exception message: " + e.getMessage();
            log.error(errorText);
            throw new DBException(errorText, e);
        }

        return keywords;
    }

    @Override
    public long save(Keyword keyword) {

        long id;

        try {
            PreparedStatement insertStatement = connection
                    .prepareStatement(SqlQueries.SAVE_KEYWORD_QUERY, Statement.RETURN_GENERATED_KEYS);
            insertStatement.setString(1, keyword.getWord());

            insertStatement.executeUpdate();

            id = DBUtils.getIdFromStatement(insertStatement);

        } catch (SQLException e) {
            String errorText = String.format("Can't save keyword: %s. Exception message: %s", keyword, e.getMessage());
            log.error(errorText);
            throw new DBException(errorText, e);
        }
        return id;
    }

    @Override
    public void update(Keyword keyword) {

        try {
            PreparedStatement updateStatement = connection
                    .prepareStatement(SqlQueries.UPDATE_KEYWORD_QUERY);
            updateStatement.setString(1, keyword.getWord());
            updateStatement.setLong(2, keyword.getId());

            updateStatement.execute();

        } catch (SQLException e) {
            String errorText = String.format("Can't update keyword: %s. Exception message: %s", keyword, e.getMessage());
            log.error(errorText);
            throw new DBException(errorText, e);
        }
    }

    @Override
    public void delete(Keyword keyword) {

        try {
            PreparedStatement deleteStatement = connection
                    .prepareStatement(SqlQueries.DELETE_KEYWORD_QUERY);
            deleteStatement.setLong(1, keyword.getId());

            deleteStatement.execute();

        } catch (SQLException e) {
            String errorText = String.format("Can't delete keyword: %s. Exception message: %s", keyword, e.getMessage());
            log.error(errorText);
            throw new DBException(errorText, e);
        }
    }

    protected Keyword getKeywordFromResultRow(ResultSet rs) throws SQLException {
        return Keyword.builder()
                .id(rs.getLong("keyword_id"))
                .word(rs.getString("word"))
                .build();
    }
}
