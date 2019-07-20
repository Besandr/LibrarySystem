package com.library.model.Services;

import com.library.model.data.DaoManager;
import com.library.model.data.DaoManagerFactory;
import com.library.model.data.dao.*;
import com.library.model.data.dto.LoanDto;
import com.library.model.data.entity.Book;
import com.library.model.data.entity.Loan;
import com.library.model.data.entity.Location;
import com.library.model.data.entity.User;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Service class which has methods bound with loan operations
 */
public class LoanService extends Service{

    private static final LoanService instance = new LoanService();

    /**
     * Saves an apply for loan which is needed to be approved
     * @param loan - a new loan at "apply for" stage
     * @return {@code true} if saving is successful
     *         and {@code false} if it is not
     */
    public boolean saveApplyForLoan(Loan loan) {

        DaoManager daoManager = DaoManagerFactory.createDaoManager();

        Object executingResult = daoManager.executeAndClose(manager -> {
            manager.getLoanDao().save(loan);
            return true;
        });

        return checkAndCastExecutingResult(executingResult);
    }

    /**
     * Gets a list with all unapproved loans.
     * @return a list with all unapproved loans
     */
    public List<LoanDto> getAllUnapprovedLoans(){

        DaoManager daoManager = DaoManagerFactory.createDaoManager();

        Object executingResult = daoManager.executeAndClose(manager -> manager.getLoanDtoDao().getAllUnapprovedLoans());

        if (Objects.nonNull(executingResult) && executingResult instanceof List) {
            return (List) executingResult;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Gets a list with all active loans (not returned yet).
     * @return a list with all active loans
     */
    public List<LoanDto> getAllActiveLoans(){

        DaoManager daoManager = DaoManagerFactory.createDaoManager();

        Object executingResult = daoManager.executeAndClose(manager -> manager.getLoanDtoDao().getAllActiveLoans());

        if (Objects.nonNull(executingResult) && executingResult instanceof List) {
            return (List) executingResult;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Gets a list with all unapproved loans of given User.
     * @param user - user whose unapproved loans need to
     *             be returned
     * @return a list with user's unapproved loans
     */
    public List<LoanDto> getUnapprovedLoansByUser(User user){

        DaoManager daoManager = DaoManagerFactory.createDaoManager();

        Object executingResult = daoManager.executeAndClose(manager -> manager.getLoanDtoDao().getUnapprovedLoansByUser(user));

        if (Objects.nonNull(executingResult) && executingResult instanceof List) {
            return (List) executingResult;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Gets a list with all active (not returned) loans of given User.
     * @param user - user whose active loans need to
     *             be returned
     * @return a list with user's active loans
     */
    public List<LoanDto> getActiveLoansByUser(User user) {

        DaoManager daoManager = DaoManagerFactory.createDaoManager();

        Object executingResult = daoManager.executeAndClose(manager -> manager.getLoanDtoDao().getActiveLoansByUser(user));

        if (Objects.nonNull(executingResult) && executingResult instanceof List) {
            return (List) executingResult;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Gets a list with all loans of given User(his loans history).
     * @param user - user whose loans need to be returned
     * @return a list with all user's loans
     */
    public List<LoanDto> getAllLoansByUser(User user) {

        DaoManager daoManager = DaoManagerFactory.createDaoManager();

        Object executingResult = daoManager.executeAndClose(manager -> manager.getLoanDtoDao().getAllLoansByUser(user));

        if (Objects.nonNull(executingResult) && executingResult instanceof List) {
            return (List) executingResult;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Gets a list with all active loans of target book.
     * @param book - book which active loans are needed to be get
     * @return a list with all active loans of target book
     */
    public List<LoanDto> getActiveLoansByBook(Book book) {

        DaoManager daoManager = DaoManagerFactory.createDaoManager();

        Object executingResult = daoManager.executeAndClose(manager -> manager.getLoanDtoDao().getActiveLoansByBook(book));

        if (Objects.nonNull(executingResult) && executingResult instanceof List) {
            return (List) executingResult;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Approves unapproved loan with given id
     * @param loanId - id of unapproved loan
     * @return {@code true} if saving changes is successful
     *          and {@code false} if it is not
     */
    public boolean approveLoan(long loanId) {

        DaoManager daoManager = DaoManagerFactory.createDaoManager();

        synchronized (this) {
            Object executingResult = daoManager.executeTransaction(manager -> approveLoanCommand(manager, loanId));
            return checkAndCastExecutingResult(executingResult);
        }
    }

    /**
     * Updates the record of active loan with given Id to change it to
     * "return" state. It represents returning a book from user to library
     * @param loanId - id of active loan
     * @return {@code true} if saving changes is successful
     *         and {@code false} if it is not
     */
    public boolean returnBook(long loanId) {

        DaoManager daoManager = DaoManagerFactory.createDaoManager();

        Object executingResult = daoManager.executeTransaction(manager -> returnBookCommand(manager, loanId));

        return checkAndCastExecutingResult(executingResult);
    }

    //Commands which is needed to be executed in corresponding public service methods
    protected boolean approveLoanCommand(DaoManager manager, long loanId) throws SQLException {

        LoanDao loanDao = (LoanDao) manager.getLoanDao();
        Optional<Loan> loan = loanDao.get(loanId);
        //check is loan with given id persistent in the DB
        //and not approved yet
        if (!loan.isPresent() || loan.get().getLoanDate() != null) {
            return EXECUTING_FAILED;
        }

        //check is there a target book on the shelves
        LocationDao locationDao = (LocationDao) manager.getLocationDao();
        Optional<Location> locationOptional = locationDao.getBookLocation(loan.get().getBookId(), true);

        if (locationOptional.isPresent()) {
            //change loan status to "approved"(set today's date
            //as loan date and expired date(plus 1 month from today)
            loanDao.updateLoanAndExpiredDate(loanId, LocalDate.now(), LocalDate.now().plusMonths(1));
            //change the status of book cell in the storage as not occupied but still reserved by this book_id
            locationDao.updateIsOccupied(locationOptional.get().getId(), false);

            return EXECUTING_SUCCESSFUL;

        } else {
            return EXECUTING_FAILED;
        }
    }

    protected boolean returnBookCommand(DaoManager manager, long loanId) throws SQLException {

        LoanDao loanDao = (LoanDao) manager.getLoanDao();
        Optional<Loan> loan;

        synchronized (this) {
            loan = loanDao.get(loanId);
            //Checking is loan with given id persistent in the DB
            //and book is not returned by user yet
            if (!loan.isPresent() || loan.get().getReturnDate() != null) {
                return EXECUTING_FAILED;
            }
            //Setting the loan status as "returned"(set today's date as return date in the loan record)
            loanDao.updateReturnDate(loanId, LocalDate.now());
            //Returning the book to the library's storage
            LocationDao locationDao = (LocationDao) manager.getLocationDao();
            Optional<Location> locationOptional = locationDao.getBookLocation(loan.get().getBookId(), false);
            locationDao.updateIsOccupied(locationOptional.get().getId(), true);
        }

        updateUsersKarma(manager, loan);

        return EXECUTING_SUCCESSFUL;
    }

    /**
     * Decrease users karma if the loan was returned after the expired date
     * @param manager - {@code DadManager} for accessing {@code Dao} is needed
     * @param loan - returned loan
     */
    protected void updateUsersKarma(DaoManager manager, Optional<Loan> loan) throws SQLException {

        LocalDate today = LocalDate.now();

        if (loan.get().getExpiredDate().isBefore(today)) {

            UserDao userDao = (UserDao) manager.getUserDao();
            userDao.updateKarma(loan.get().getUserId(), -1);
        }
    }

    public static LoanService getInstance() {
        return instance;
    }

    private LoanService(){}

}
