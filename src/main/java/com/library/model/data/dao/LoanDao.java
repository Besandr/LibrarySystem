package com.library.model.data.dao;

import com.library.model.data.entity.Loan;

import java.time.LocalDate;

public interface LoanDao extends Dao<Loan> {

    void updateLoanAndExpiredDate(long loanId, LocalDate loanDate, LocalDate expiredDate);

    void updateReturnDate(long loanId, LocalDate returnDate);
}
