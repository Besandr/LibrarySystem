package com.library.model.data.dao;

import com.library.model.data.entity.Loan;

import java.time.LocalDate;
import java.util.List;

/**
 * Dao for Books entity
 */
public interface LoanDao extends Dao<Loan> {

    /**
     * Changes loan and expired dates of given loan
     * @param loanId - id of loan to be changed
     * @param loanDate - new loan date
     * @param expiredDate - new expired date
     */
    void updateLoanAndExpiredDate(long loanId, LocalDate loanDate, LocalDate expiredDate);

    /**
     * Changes return date of given loan
     * @param loanId - id of loan to be changed
     * @param returnDate - new returned date
     */
    void updateReturnDate(long loanId, LocalDate returnDate);

    /**
     * Gets a list of unapproved loans
     * @return - a list with unaproved loans
     *          or the empty list if there is no any
     */
    List<Loan> getUnapprovedLoans();
}
