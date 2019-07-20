package com.library.model.data.dao;

import com.library.model.data.dto.LoanDto;
import com.library.model.data.entity.Book;
import com.library.model.data.entity.User;

import java.util.List;

public interface LoanDtoDao {

    /**
     * Gets a list of all unapproved loans.
     * @return - a list with unapproved loans
     *          or the empty list if there is no any
     */
    List<LoanDto> getAllUnapprovedLoans();

    /**
     * Gets a list of active(not returned) loans.
     * @return - a list with active loans
     *          or the empty list if there is no any
     */
    List<LoanDto> getAllActiveLoans();

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
     * @param book - a target reading by users book
     * @return - a list with active loans for target book
     *          or the empty list if there is no any
     */
    List<LoanDto> getActiveLoansByBook(Book book);
}
