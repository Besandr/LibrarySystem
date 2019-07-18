package com.library.model.data.dao;

import com.library.model.data.dto.LoanDto;
import com.library.model.data.entity.Book;

import java.util.List;

public interface LoanDtoDao {

    /**
     * Gets a list of unapproved loans.
     * @return - a list with unapproved loans
     *          or the empty list if there is no any
     */
    List<LoanDto> getUnapprovedLoans();

    /**
     * Gets a list of active (not returned) loans.
     * @return - a list with active loans
     *          or the empty list if there is no any
     */
    List<LoanDto> getActiveLoans();

    /**
     * Gets a list of active (not returned) loans for
     * target book.
     * @param book - a target reading by users book
     * @return - a list with active loans for target book
     *          or the empty list if there is no any
     */
    List<LoanDto> getActiveLoansByBook(Book book);
}
