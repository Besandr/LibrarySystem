package com.library.repository.dao.impl;

import com.library.repository.DBUtils;
import com.library.repository.DaoManager;
import com.library.repository.dao.LoanDao;
import com.library.repository.dao.LoanDtoDao;
import com.library.repository.dto.LoanDto;
import com.library.repository.entity.Book;
import com.library.repository.entity.Loan;
import com.library.repository.entity.User;
import com.library.repository.DaoException;
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
public class LoanDtoDaoImpl implements LoanDtoDao {

    private static final Logger log = LogManager.getLogger(LoanDao.class);

    private DaoManager manager;
    private Connection connection;

    public LoanDtoDaoImpl(DaoManager manager, Connection connection) {
        this.manager = manager;
        this.connection = connection;
    }

    /**
     * {@inheritDoc}
     */
    public List<LoanDto> getUnapprovedLoans(int limit, int offset) {
        return createLoanDtoListFromQuery(DBQueries.GET_UNAPPROVED_LOANS_QUERY, limit, offset);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getUnapprovedLoansQuantity() {
        try{
            return DBUtils.getResultOfCountingQuery(connection, DBQueries.GET_UNAPPROVED_LOANS_QUANTITY);
        } catch (SQLException e) {
            String errorText = "Can't get unapproved loans quantity";
            log.error(errorText, e);
            throw new DaoException(errorText, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LoanDto> getActiveLoans(int limit, int offset) {
        return createLoanDtoListFromQuery(DBQueries.GET_ACTIVE_LOANS_QUERY, limit, offset);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getActiveLoansQuantity() {
        try{
            return DBUtils.getResultOfCountingQuery(connection, DBQueries.GET_ACTIVE_LOANS_QUANTITY);
        } catch (SQLException e) {
            String errorText = "Can't get active loans quantity";
            log.error(errorText, e);
            throw new DaoException(errorText, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LoanDto> getUnapprovedLoansByUserId(long userId, int limit, int offset) {

        try{
            PreparedStatement statement = connection.prepareStatement(DBQueries.GET_UNAPPROVED_LOANS_BY_USER_QUERY);
            statement.setLong(1, userId);
            statement.setInt(2, limit);
            statement.setInt(3, offset);

            return createLoanDtoListFromResultSet(statement.executeQuery());

        } catch (SQLException e) {
            String errorText = String.format("Can't get unapproved loans by user. User: %s. Cause: %s.", userId, e.getMessage());
            log.error(errorText, e);
            throw new DaoException(errorText, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getUnapprovedLoansByUserIdQuantity(long userId) {
        try{
            PreparedStatement statement = connection.prepareStatement(DBQueries.GET_UNAPPROVED_LOANS_BY_USER_QUANTITY);
            statement.setLong(1, userId);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getLong(1);
            } else {
                return 0;
            }
        } catch (SQLException e) {
            String errorText = "Can't get user's unapproved loans quantity";
            log.error(errorText, e);
            throw new DaoException(errorText, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LoanDto> getActiveLoansByUserId(long userId, int limit, int offset) {

        try{
            PreparedStatement statement = connection.prepareStatement(DBQueries.GET_ACTIVE_LOANS_BY_USER_QUERY);
            statement.setLong(1, userId);
            statement.setInt(2, limit);
            statement.setInt(3, offset);

            return createLoanDtoListFromResultSet(statement.executeQuery());

        } catch (SQLException e) {
            String errorText = String.format("Can't get active loans for given user. User: %s. Cause: %s", userId, e.getMessage());
            log.error(errorText, e);
            throw new DaoException(errorText, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getReturnedLoansByUserIdQuantity(long userId) {
        try{
            PreparedStatement statement = connection.prepareStatement(DBQueries.GET_RETURNED_LOANS_BY_USER_QUANTITY);
            statement.setLong(1, userId);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getLong(1);
            } else {
                return 0;
            }
        } catch (SQLException e) {
            String errorText = "Can't get user's returned loans quantity";
            log.error(errorText, e);
            throw new DaoException(errorText, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LoanDto> getReturnedLoansByUserId(long userId, int limit, int offset) {
        try{
            PreparedStatement statement = connection.prepareStatement(DBQueries.GET_RETURNED_LOANS_BY_USER_QUERY);
            statement.setLong(1, userId);
            statement.setInt(2, limit);
            statement.setInt(3, offset);

            return createLoanDtoListFromResultSet(statement.executeQuery());

        } catch (SQLException e) {
            String errorText = String.format("Can't get all loans by user. User: %s. Cause: %s.", userId, e.getMessage());
            log.error(errorText, e);
            throw new DaoException(errorText, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getActiveLoansByUserIdQuantity(long userId) {
        try{
            PreparedStatement statement = connection.prepareStatement(DBQueries.GET_ACTIVE_LOANS_BY_USER_QUANTITY);
            statement.setLong(1, userId);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getLong(1);
            } else {
                return 0;
            }
        } catch (SQLException e) {
            String errorText = "Can't get user's active loans quantity";
            log.error(errorText, e);
            throw new DaoException(errorText, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LoanDto> getActiveLoansByBookId(long bookId) {
        try{
            PreparedStatement statement = connection.prepareStatement(DBQueries.GET_ACTIVE_LOANS_BY_BOOK_QUERY);
            statement.setLong(1, bookId);

            ResultSet rs = statement.executeQuery();

            return createLoanDtoListFromResultSet(rs);

        } catch (SQLException e) {
            String errorText = "Can't get active loanDtos list from DB. Cause: " + e.getMessage();
            log.error(errorText, e);
            throw new DaoException(errorText, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LoanDto> getUnapprovedLoansByBookId(long bookId) {
        try{
            PreparedStatement statement = connection.prepareStatement(DBQueries.GET_UNAPPROVED_LOANS_BY_BOOK_QUERY);
            statement.setLong(1, bookId);

            ResultSet rs = statement.executeQuery();

            return createLoanDtoListFromResultSet(rs);

        } catch (SQLException e) {
            String errorText = "Can't get unapproved loanDtos list from DB. Cause: " + e.getMessage();
            log.error(errorText, e);
            throw new DaoException(errorText, e);
        }
    }

    /**
     * Creates a {@code List} and fills it with LoanDto objects received from
     * executing a given query.
     * @param query - query which result is need to be returned
     * @param limit the number of loans returned
     * @param offset of the first loan to return
     * @return - a {@code List} with results of executing the given query.
     * {@code List} can be empty if query has no results
     */
    protected List<LoanDto> createLoanDtoListFromQuery(String query, int limit, int offset) {
        try {
            PreparedStatement selectStatement = connection.prepareStatement(query);
            selectStatement.setInt(1, limit);
            selectStatement.setInt(2, offset);

            ResultSet rs = selectStatement.executeQuery();

            return createLoanDtoListFromResultSet(rs);

        } catch (SQLException e) {
            String errorText = "Can't get loanDtos list from DB. Cause: " + e.getMessage();
            log.error(errorText, e);
            throw new DaoException(errorText, e);
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

            Loan loan = ((LoanDaoImpl) manager.getLoanDao()).getLoanFromResultRow(rs);
            Book book = ((BookDaoImpl) manager.getBookDao()).getBookFromResultRow(rs);
            User user = ((UserDaoImpl) manager.getUserDao()).getUserFromResultRow(rs);

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
