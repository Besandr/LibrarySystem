package com.library.repository.dao.impl;

import com.library.repository.DBUtils;
import com.library.repository.dao.LoanDao;
import com.library.repository.entity.Loan;
import com.library.repository.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementing of AuthorDao for working with a MySql server
 */
public class LoanDaoImpl implements LoanDao {

    private static final Logger log = LogManager.getLogger(LoanDao.class);

    private Connection connection;

    public LoanDaoImpl(Connection connection) {
        this.connection = connection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Loan> get(long loanId) {

        Optional<Loan> resultOptional = Optional.empty();

        try {
            PreparedStatement getLoanStatement = connection
                    .prepareStatement(DBQueries.GET_LOAN_QUERY);
            getLoanStatement.setLong(1, loanId);

            ResultSet rs = getLoanStatement.executeQuery();

            if (rs.next()) {
                resultOptional = Optional.of(getLoanFromResultRow(rs));
            }

            rs.close();

        } catch (SQLException e) {
            String errorText = String.format("Can't get loan by id: %s. Cause: %s", loanId, e.getMessage());
            log.error(errorText, e);
            throw new DaoException(errorText, e);
        }

        return resultOptional;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Loan> getAll() {

        return createLoansListFromQuery(DBQueries.ALL_LOANS_QUERY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long save(Loan loan) {

        try {
            PreparedStatement insertStatement = connection
                    .prepareStatement(DBQueries.SAVE_LOAN_QUERY, Statement.RETURN_GENERATED_KEYS);
            insertStatement.setLong(1, loan.getBookId());
            insertStatement.setLong(2, loan.getUserId());
            insertStatement.setObject(3, loan.getApplyDate());

            insertStatement.executeUpdate();

            return DBUtils.getIdFromStatement(insertStatement);

        } catch (SQLException e) {
            if (DBUtils.isTryingToInsertDuplicate(e)) {
                return -1;
            } else {
                String errorText = String.format("Can't save loan: %s. Cause: %s", loan, e.getMessage());
                log.error(errorText, e);
                throw new DaoException(errorText, e);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Loan loan) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateLoanAndExpiredDate(long loanId, LocalDate loanDate, LocalDate expiredDate) {

        try {
            PreparedStatement updateStatement = connection.prepareStatement(DBQueries.UPDATE_LOAN_AND_EXPIRED_DATE_QUERY);
            updateStatement.setObject(1, loanDate);
            updateStatement.setObject(2, expiredDate);
            updateStatement.setLong(3, loanId);

            updateStatement.execute();

        } catch (SQLException e) {
            String errorText = String.format("Can't update loan and expired dates. id: %s." +
                    " loanDate: %s. expiredDate: %s. Cause: %s", loanId, loanDate, expiredDate, e.getMessage());
            log.error(errorText, e);
            throw new DaoException(errorText, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateReturnDate(long loanId, LocalDate returnDate) {

        try {
            PreparedStatement updateStatement = connection.prepareStatement(DBQueries.UPDATE_RETURN_DATE_QUERY);
            updateStatement.setObject(1, returnDate);
            updateStatement.setLong(2, loanId);

            updateStatement.execute();

        } catch (SQLException e) {
            String errorText = String.format("Can't update return date. id: %s." +
                    " Return date: %s. Cause: %s", loanId, returnDate, e.getMessage());
            log.error(errorText, e);
            throw new DaoException(errorText, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Loan loan) {

        try {
            PreparedStatement deleteStatement = connection
                    .prepareStatement(DBQueries.DELETE_LOAN_QUERY);
            deleteStatement.setLong(1, loan.getId());

            deleteStatement.execute();

        } catch (SQLException e) {
            String errorText = String.format("Can't delete loan: %s. Cause: %s", loan, e.getMessage());
            log.error(errorText, e);
            throw new DaoException(errorText, e);
        }
    }

    /**
     * Takes a "select" query, executes it and create Loans list
     * from query results
     * @param query - DB query "select" string
     * @return - loans list contains the result of the given query
     */
    protected List<Loan> createLoansListFromQuery(String query) {
        List<Loan> loans = new ArrayList<>();

        try {
            PreparedStatement selectStatement = connection
                    .prepareStatement(query);

            ResultSet rs = selectStatement.executeQuery();

            while (rs.next()) {
                loans.add(getLoanFromResultRow(rs));
            }

            rs.close();

        } catch (SQLException e) {
            String errorText = "Can't get loans list from DB. Cause: " + e.getMessage();
            log.error(errorText, e);
            throw new DaoException(errorText, e);
        }

        return loans;
    }

    protected Loan getLoanFromResultRow(ResultSet rs) throws SQLException {

        Loan loan = Loan.builder()
                .id(rs.getLong("loan_id"))
                .bookId(rs.getLong("book_id"))
                .userId(rs.getLong("user_id"))
                .applyDate(rs.getObject("apply_date", LocalDate.class))
                .loanDate(rs.getObject("loan_date", LocalDate.class))
                .expiredDate(rs.getObject("expired_date", LocalDate.class))
                .returnDate(rs.getObject("return_date", LocalDate.class))
                .build();

        return loan;
    }
}
