package com.library.model.data.dao;

import com.library.model.data.DaoManager;
import com.library.model.data.dto.LoanDto;
import com.library.model.data.entity.Book;
import com.library.model.data.entity.Loan;
import com.library.model.data.entity.User;
import com.library.model.exceptions.DBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains methods for creating LoanDto by getting their data
 * from select query to DB
 */
public class MySqlLoanDtoDao implements LoanDtoDao{

    private static final Logger log = LogManager.getLogger(LoanDao.class);

    private DaoManager manager;
    private Connection connection;

    public MySqlLoanDtoDao(DaoManager manager, Connection connection) {
        this.manager = manager;
        this.connection = connection;
    }

    /**
     * {@inheritDoc}
     */
    public List<LoanDto> getAllUnapprovedLoans() {
        return createLoanDtoListFromQuery(SqlQueries.GET_UNAPPROVED_LOANS_QUERY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LoanDto> getUnapprovedLoansByUser(User user) {

        try{
            PreparedStatement statement = connection.prepareStatement(SqlQueries.GET_UNAPPROVED_LOANS_BY_USER_QUERY);
            statement.setLong(1, user.getId());

            return createLoanDtoListFromResultSet(statement.executeQuery());

        } catch (SQLException e) {
            String errorText = String.format("Can't get unapproved loans by user. User: %s. Cause: %s.", user, e.getMessage());
            log.error(errorText);
            throw new DBException(errorText, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LoanDto> getAllActiveLoans() {
        return createLoanDtoListFromQuery(SqlQueries.GET_ACTIVE_LOANS_QUERY);
    }

    @Override
    public List<LoanDto> getActiveLoansByBook(Book book) {

        try{
            PreparedStatement statement = connection.prepareStatement(SqlQueries.GET_ACTIVE_LOANS_BY_BOOK_QUERY);
            statement.setLong(1, book.getId());

            ResultSet rs = statement.executeQuery();

            return createLoanDtoListFromResultSet(rs);

        } catch (SQLException e) {
            String errorText = "Can't get loanDtos list from DB. Cause: " + e.getMessage();
            log.error(errorText);
            throw new DBException(errorText, e);
        }
    }

    /**
     * Creates a {@code List} and fills it with LoanDto objects received from
     * executing a given query.
     * @param query - query which result is need to be returned
     * @return - a {@code List} with results of executing the given query.
     * {@code List} can be empty if query has no results
     */
    protected List<LoanDto> createLoanDtoListFromQuery(String query) {

        try {
            PreparedStatement selectStatement = connection.prepareStatement(query);

            ResultSet rs = selectStatement.executeQuery();

            return createLoanDtoListFromResultSet(rs);

        } catch (SQLException e) {
            String errorText = "Can't get loanDtos list from DB. Cause: " + e.getMessage();
            log.error(errorText);
            throw new DBException(errorText, e);
        }
    }

    /**
     * Creates a {@code List} and fills it with LoanDto objects received
     * from given {@code ResultSet}
     * @param rs - set with data which is need to be returned
     * @return - a {@code List} with the data from given {@code ResultSet}.
     * {@code List} can be empty if the given set has no data
     */
    protected List<LoanDto> createLoanDtoListFromResultSet(ResultSet rs) throws SQLException {

        List<LoanDto> loanDtos = new ArrayList<>();

        while (rs.next()) {

            Loan loan = ((MySqlLoanDao) manager.getLoanDao()).getLoanFromResultRow(rs);
            Book book = ((MySqlBookDao) manager.getBookDao()).getBookFromResultRow(rs);
            User user = ((MySqlUserDao) manager.getUserDao()).getUserFromResultRow(rs);

            LoanDto dto = LoanDto.builder()
                    .loan(loan)
                    .book(book)
                    .user(user)
                    .build();

            loanDtos.add(dto);
        }

        rs.close();

        return loanDtos;
    }
}
