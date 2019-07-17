package com.library.model.data.dao;

import com.library.model.data.DBUtils;
import com.library.model.data.entity.Book;
import com.library.model.data.entity.Loan;
import com.library.model.data.entity.User;
import com.library.model.exceptions.DBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MySqlLoanDao implements LoanDao {

    private static final Logger log = LogManager.getLogger(LoanDao.class);

    private Connection connection;

    public MySqlLoanDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Loan> get(long loanId) {

        Optional<Loan> resultOptional = Optional.empty();

        try {
            PreparedStatement getLoanStatement = connection
                    .prepareStatement(SqlQueries.GET_LOAN_QUERY);
            getLoanStatement.setLong(1, loanId);

            ResultSet rs = getLoanStatement.executeQuery();

            if (rs.next()) {
                resultOptional = Optional.of(getLoanFromResultRow(rs));
            }

            rs.close();

        } catch (SQLException e) {
            String errorText = String.format("Can't get loan by id: %s. Cause: %s", loanId, e.getMessage());
            log.error(errorText);
            throw new DBException(errorText, e);
        }

        return resultOptional;
    }

    @Override
    public List<Loan> getAll() {

        List<Loan> loans = new ArrayList<>();

        try {
            PreparedStatement selectStatement = connection
                    .prepareStatement(SqlQueries.ALL_LOANS_QUERY);
            ResultSet rs = selectStatement.executeQuery();

            while (rs.next()) {
                loans.add(getLoanFromResultRow(rs));
            }

            rs.close();

        } catch (SQLException e) {
            String errorText = "Can't get loans list from DB. Cause: " + e.getMessage();
            log.error(errorText);
            throw new DBException(errorText, e);
        }

        return loans;
    }

    @Override
    public long save(Loan loan) {

        try {
            PreparedStatement insertStatement = connection
                    .prepareStatement(SqlQueries.SAVE_LOAN_QUERY, Statement.RETURN_GENERATED_KEYS);
            insertStatement.setLong(1, loan.getBook().getId());
            insertStatement.setLong(2, loan.getUser().getId());
            insertStatement.setObject(3, loan.getApplyDate());

            insertStatement.executeUpdate();

            return DBUtils.getIdFromStatement(insertStatement);

        } catch (SQLException e) {
            if (DBUtils.isTryingToInsertDuplicate(e)) {
                return -1;
            } else {
                String errorText = String.format("Can't save loan: %s. Cause: %s", loan, e.getMessage());
                log.error(errorText);
                throw new DBException(errorText, e);
            }
        }
    }

    @Override
    public void update(Loan loan) {
    }

    @Override
    public void updateLoanAndExpiredDate(long loanId, LocalDate loanDate, LocalDate expiredDate) {

        try {
            PreparedStatement updateStatement = connection.prepareStatement(SqlQueries.UPDATE_LOAN_AND_EXPIRED_DATE_QUERY);
            updateStatement.setObject(1, loanDate);
            updateStatement.setObject(2, expiredDate);
            updateStatement.setLong(3, loanId);

            updateStatement.execute();

        } catch (SQLException e) {
            String errorText = String.format("Can't update loan and expired dates. id: %s." +
                    " loanDate: %s. expiredDate: %s. Cause: %s", loanId, loanDate, expiredDate, e.getMessage());
            log.error(errorText);
            throw new DBException(errorText, e);
        }
    }

    @Override
    public void updateReturnDate(long loanId, LocalDate returnDate) {

        try {
            PreparedStatement updateStatement = connection.prepareStatement(SqlQueries.UPDATE_RETURN_DATE_QUERY);
            updateStatement.setObject(1, returnDate);
            updateStatement.setLong(2, loanId);

            updateStatement.execute();

        } catch (SQLException e) {
            String errorText = String.format("Can't update return date. id: %s." +
                    " Return date: %s. Cause: %s", loanId, returnDate, e.getMessage());
            log.error(errorText);
            throw new DBException(errorText, e);
        }
    }

    @Override
    public void delete(Loan loan) {

        try {
            PreparedStatement deleteStatement = connection
                    .prepareStatement(SqlQueries.DELETE_LOAN_QUERY);
            deleteStatement.setLong(1, loan.getId());

            deleteStatement.execute();

        } catch (SQLException e) {
            String errorText = String.format("Can't delete loan: %s. Cause: %s", loan, e.getMessage());
            log.error(errorText);
            throw new DBException(errorText, e);
        }
    }

    protected Loan getLoanFromResultRow(ResultSet rs) throws SQLException {

        Loan loan = Loan.builder()
                .id(rs.getLong("loan_id"))
                .book(Book.builder().id(rs.getLong("book_id")).build())
                .user(User.builder().id(rs.getLong("user_id")).build())
                .applyDate(rs.getObject("apply_date", LocalDate.class))
                .loanDate(rs.getObject("loan_date", LocalDate.class))
                .expiredDate(rs.getObject("expired_date", LocalDate.class))
                .returnDate(rs.getObject("return_date", LocalDate.class))
                .build();

        return loan;
    }
}
