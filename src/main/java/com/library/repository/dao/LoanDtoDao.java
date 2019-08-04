package com.library.repository.dao;

import com.library.repository.dto.LoanDto;
import com.library.repository.entity.User;

import java.util.List;

public interface LoanDtoDao {

    /**
     * Gets a list of unapproved loans in chosen range.
     * @return - a list with unapproved loans
     *          or the empty list if there is no any
     * @param limit the number of loans returned
     * @param offset of the first loan to return
     */
    List<LoanDto> getUnapprovedLoans(int limit, int offset);

    /**
     * Gives a quantity of all unapproved loans
     * @return a quantity of all unapproved loans
     */
    long getUnapprovedLoansQuantity();

    /**
     * Gets a list of active(not returned) loans.
     * @return - a list with active loans
     *          or the empty list if there is no any
     * @param limit the number of loans returned
     * @param offset the number of loans returned
     */
    List<LoanDto> getActiveLoans(int limit, int offset);

    /**
     * Gives a quantity of all active loans
     * @return a quantity of all active loans
     */
    long getActiveLoansQuantity();

    /**
     * Gets a list of unapproved loans for target user.
     * @param user - a target user whose loans is need to be returned
     * @return - a list with unapproved loans for target user
     *          or the empty list if there is no any
     */
    List<LoanDto> getUnapprovedLoansByUser(User user);

    /**
     * Gets a list of active(not returned) loans for
     * target user.
     * @param user - a target user whose loans is need to be returned
     * @return - a list with active loans for target user
     *          or the empty list if there is no any
     */
    List<LoanDto> getActiveLoansByUser(User user);

    /**
     * Gets a list of all loans for target user.
     * @param user - a target user whose loans is need to be returned
     * @return - a list with all loans for target user
     *          or the empty list if there is no any
     */
    List<LoanDto> getAllLoansByUser(User user);

    /**
     * Gets a list of active (not returned) loans for
     * target book.
     * @param bookId - ID of a target reading by users book
     * @return - a list with active loans for target book
     *          or the empty list if there is no any
     */
    List<LoanDto> getActiveLoansByBookId(long bookId);

    /**
     * Gets a list of unapproved  loans for target book.
     * @param bookId - ID of a target reading by users book
     * @return - a list with active loans for target book
     *          or the empty list if there is no any
     */
    List<LoanDto> getUnapprovedLoansByBookId(long bookId);
}
