package com.library.repository.dao;

import com.library.repository.entity.Loan;

import java.time.LocalDate;

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
}
