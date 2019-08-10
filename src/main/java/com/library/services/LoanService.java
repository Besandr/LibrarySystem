package com.library.services;

import com.library.repository.DaoManager;
import com.library.repository.DaoManagerFactory;
import com.library.repository.dao.AuthorDao;
import com.library.repository.dao.LoanDao;
import com.library.repository.dao.LocationDao;
import com.library.repository.dao.UserDao;
import com.library.repository.dto.LoanDto;
import com.library.repository.entity.Loan;
import com.library.repository.entity.Location;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service class which has methods bound with loan operations
 * and DAO
 */
public class LoanService extends Service{

    private DaoManagerFactory daoManagerFactory;

    LoanService(DaoManagerFactory daoManagerFactory) {
        this.daoManagerFactory = daoManagerFactory;
    }

    /**
     * Saves an apply for book by creating a loan which
     * is needed to be approved
     * @param bookId - ID of ordered book
     * @param userId - ID of user ordered the book
     * @return {@code true} if saving is successful
     * and {@code false} if it is not
     */
    public boolean saveApplyForBook(long bookId, long userId) {

        DaoManager daoManager = daoManagerFactory.createDaoManager();

        Loan loan = Loan.builder()
                .bookId(bookId)
                .userId(userId)
                .applyDate(LocalDate.now())
                .build();

        Object executingResult = daoManager.executeAndClose(manager -> {
            manager.getLoanDao().save(loan);
            return true;
        });

        return checkAndCastExecutingResult(executingResult);
    }

    /**
     * Gets a list with unapproved loans in requested range.
     * @return a list with unapproved loans
     * @param previousRecordNumber - number of previous record
     * @param recordsQuantity - quantity of records per page
     */
    public List<LoanDto> getUnapprovedLoans(int recordsQuantity, int previousRecordNumber){

        DaoManager daoManager = daoManagerFactory.createDaoManager();

        Object executingResult = daoManager.executeAndClose(manager -> getAllUnapprovedLoansCommand(manager, recordsQuantity, previousRecordNumber));

        return checkAndCastObjectToList(executingResult);
    }

    /**
     * Gives a quantity of all unapproved loans
     * @return a quantity of all unapproved loans
     */
    public long getUnapprovedLoansQuantity(){
        DaoManager daoManager = daoManagerFactory.createDaoManager();

        Object executingResult = daoManager.executeAndClose(manager -> manager.getLoanDtoDao().getUnapprovedLoansQuantity());

        return checkAndCastObjectToLong(executingResult);
    }

    /**
     * Gets a list with all active loans (not returned yet).
     * @param recordsQuantity - quantity of records per page
     * @param previousRecordNumber - number of previous record
     * @return a list with all active loans
     */
    public List<LoanDto> getActiveLoans(int recordsQuantity, int previousRecordNumber){

        DaoManager daoManager = daoManagerFactory.createDaoManager();

        Object executingResult = daoManager.executeAndClose(manager -> getActiveLoansCommand(manager, recordsQuantity, previousRecordNumber));

        return checkAndCastObjectToList(executingResult);
    }

    /**
     * Gives a quantity of all active loans
     * @return a quantity of all active loans
     */
    public long getActiveLoansQuantity(){
        DaoManager daoManager = daoManagerFactory.createDaoManager();

        Object executingResult = daoManager.executeAndClose(manager -> manager.getLoanDtoDao().getActiveLoansQuantity());

        return checkAndCastObjectToLong(executingResult);
    }

    /**
     * Gets a list with all unapproved loans of given User.
     * @param userId - ID of user whose unapproved loans need to
     *             be returned
     * @param previousRecordNumber - number of previous record
     * @param recordsQuantity - quantity of records per page
     * @return a list with user's unapproved loans
     */
    public List<LoanDto> getUnapprovedLoansByUser(long userId, int recordsQuantity, int previousRecordNumber){

        DaoManager daoManager = daoManagerFactory.createDaoManager();

        Object executingResult = daoManager.executeAndClose(manager -> getUnapprovedLoansByUserCommand(manager, userId, recordsQuantity, previousRecordNumber));

        return checkAndCastObjectToList(executingResult);

    }

    /**
     * Gets a list with all active (not returned) loans of given User.
     * @param userId - ID of user whose active loans need to
     *             be returned
     * @param previousRecordNumber - number of previous record
     * @param recordsQuantity - quantity of records per page
     * @return a list with user's active loans
     */
    public List<LoanDto> getActiveLoansByUser(long userId, int recordsQuantity, int previousRecordNumber) {

        DaoManager daoManager = daoManagerFactory.createDaoManager();

        Object executingResult = daoManager.executeAndClose(manager -> getActiveLoansByUserCommand(manager, userId, recordsQuantity, previousRecordNumber));

        return checkAndCastObjectToList(executingResult);

    }

    /**
     * Gets a list with all loans of given User(his loans history).
     * @param userId - ID of user whose returned loans need to
     *             be returned from method
     * @param previousRecordNumber - number of previous record
     * @param recordsQuantity - quantity of records per page
     * @return a list with all user's loans
     */
    public List<LoanDto> getReturnedLoansByUser(long userId, int recordsQuantity, int previousRecordNumber) {

        DaoManager daoManager = daoManagerFactory.createDaoManager();

        Object executingResult = daoManager.executeAndClose(manager -> getReturnedLoansByUserCommand(manager, userId, recordsQuantity, previousRecordNumber));

        return checkAndCastObjectToList(executingResult);

    }

    /**
     * Gives a quantity of all unapproved loans of user
     * @param userId - ID of a target user whose loans quantity is need to be returned
     * @return a quantity of all unapproved loans
     */
    public long getUnapprovedLoansByUserQuantity(long userId){
        DaoManager daoManager = daoManagerFactory.createDaoManager();

        Object executingResult = daoManager.executeAndClose(manager -> manager.getLoanDtoDao().getUnapprovedLoansByUserIdQuantity(userId));

        return checkAndCastObjectToLong(executingResult);
    }

    /**
     * Gives a quantity of all active loans of user
     * @param userId - ID of a target user whose loans quantity is need to be returned
     * @return a quantity of all unapproved loans
     */
    public long getActiveLoansByUserQuantity(long userId){
        DaoManager daoManager = daoManagerFactory.createDaoManager();

        Object executingResult = daoManager.executeAndClose(manager -> manager.getLoanDtoDao().getActiveLoansByUserIdQuantity(userId));

        return checkAndCastObjectToLong(executingResult);
    }

    /**
     * Gives a quantity of all returned loans of user
     * @param userId - ID of a target user whose loans quantity is need to be returned
     * @return a quantity of all unapproved loans
     */
    public long getReturnedLoansByUserQuantity(long userId){
        DaoManager daoManager = daoManagerFactory.createDaoManager();

        Object executingResult = daoManager.executeAndClose(manager -> manager.getLoanDtoDao().getReturnedLoansByUserIdQuantity(userId));

        return checkAndCastObjectToLong(executingResult);
    }

    /**
     * Gets a list with all active loans of target book.
     * @param bookId - ID of book which active loans are needed to be gotten
     * @return a list with all active loans of target book
     */
    public List<LoanDto> getActiveLoansByBook(long bookId) {

        DaoManager daoManager = daoManagerFactory.createDaoManager();

        Object executingResult = daoManager.executeAndClose(manager -> getActiveLoansByBookCommand(manager, bookId));

        return checkAndCastObjectToList(executingResult);

    }

    /**
     * Approves unapproved loan with given id
     * @param loanId - id of unapproved loan
     * @return {@code true} if saving changes is successful
     *          and {@code false} if it is not
     */
    public boolean approveLoan(long loanId) {

        DaoManager daoManager = daoManagerFactory.createDaoManager();

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

        DaoManager daoManager = daoManagerFactory.createDaoManager();

        Object executingResult = daoManager.executeTransaction(manager -> returnBookCommand(manager, loanId));

        return checkAndCastExecutingResult(executingResult);
    }

    //Commands which is needed to be executed in corresponding public service methods

    List<LoanDto> getAllUnapprovedLoansCommand(DaoManager manager, int recordsQuantity, int previousRecordNumber) throws SQLException {
        List<LoanDto> loanDtos = manager.getLoanDtoDao().getUnapprovedLoans(recordsQuantity, previousRecordNumber);

        addAuthorsToLoanDtoInList(manager, loanDtos);
        addBookQuantityToLoanDtoInList(manager, loanDtos);

        return loanDtos;
    }

    List<LoanDto> getUnapprovedLoansByUserCommand(DaoManager manager, long userId, int recordsQuantity, int previousRecordNumber) throws SQLException {
        List<LoanDto> loanDtoList = manager.getLoanDtoDao().
                getUnapprovedLoansByUserId(userId, recordsQuantity, previousRecordNumber);

        addAuthorsToLoanDtoInList(manager, loanDtoList);
        return loanDtoList;
    }

    List<LoanDto> getActiveLoansCommand(DaoManager manager, int recordsQuantity, int previousRecordNumber) throws SQLException {
        List<LoanDto> loanDtos = manager.getLoanDtoDao().getActiveLoans(recordsQuantity, previousRecordNumber);
        addAuthorsToLoanDtoInList(manager, loanDtos);
        return loanDtos;
    }

    List<LoanDto> getActiveLoansByUserCommand(DaoManager manager, long userId, int recordsQuantity, int previousRecordNumber) throws SQLException {
        List<LoanDto> loanDtoList = manager.getLoanDtoDao()
                .getActiveLoansByUserId(userId, recordsQuantity, previousRecordNumber);

        addAuthorsToLoanDtoInList(manager, loanDtoList);
        return loanDtoList;
    }

    List<LoanDto> getReturnedLoansByUserCommand(DaoManager manager, long userId, int recordsQuantity, int previousRecordNumber) throws SQLException {
        List<LoanDto> loanDtoList = manager.getLoanDtoDao()
                .getReturnedLoansByUserId(userId, recordsQuantity, previousRecordNumber);

        addAuthorsToLoanDtoInList(manager, loanDtoList);
        return loanDtoList;
    }

    List<LoanDto> getActiveLoansByBookCommand(DaoManager manager, long bookId) throws SQLException {
        List<LoanDto> loanDtoList = manager.getLoanDtoDao().getActiveLoansByBookId(bookId);
        addAuthorsToLoanDtoInList(manager, loanDtoList);
        return loanDtoList;
    }

    synchronized boolean approveLoanCommand(DaoManager manager, long loanId) throws SQLException {

        LoanDao loanDao = manager.getLoanDao();
        Optional<Loan> loan = loanDao.get(loanId);
        //check is loan with given id persistent in the DB
        //and not approved yet
        if (!loan.isPresent() || loan.get().getLoanDate() != null) {
            return EXECUTING_FAILED;
        }

        //check is there a target book on the shelves
        LocationDao locationDao = manager.getLocationDao();
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

    boolean returnBookCommand(DaoManager manager, long loanId) throws SQLException {

        LoanDao loanDao = manager.getLoanDao();
        Optional<Loan> loanOptional;

        synchronized (this) {
            loanOptional = loanDao.get(loanId);
            //Checking is loan with given id persistent in the DB
            //and book is not returned by user yet
            if (!loanOptional.isPresent() || loanOptional.get().getReturnDate() != null) {
                return EXECUTING_FAILED;
            }

            //Returning the book to the library's storage
            LocationDao locationDao = manager.getLocationDao();
            Optional<Location> locationOptional = locationDao.getBookLocation(loanOptional.get().getBookId(), false);
            if (!locationOptional.isPresent()) {
                return EXECUTING_FAILED;
            }
            locationDao.updateIsOccupied(locationOptional.get().getId(), true);

            //Setting the loan status as "returned"(set today's date as return date in the loan record)
            loanDao.updateReturnDate(loanId, LocalDate.now());

            updateUsersKarma(manager, loanOptional.get());

        }
        return EXECUTING_SUCCESSFUL;
    }

    /**
     * Decrease users karma if the loan was returned after the expired date
     * @param manager - {@code DadManager} for accessing {@code Dao} is needed
     * @param loan - returned loan
     */
    void updateUsersKarma(DaoManager manager, Loan loan) throws SQLException {

        LocalDate today = LocalDate.now();

        if (loan.getExpiredDate().isBefore(today)) {
            UserDao userDao = manager.getUserDao();
            userDao.updateKarma(loan.getUserId(), -1);
        }
    }

    /**
     * Adds authors list to each {@code LoanDto} in the given list
     */
    void addAuthorsToLoanDtoInList(DaoManager manager, List<LoanDto> loanDtos) throws SQLException {
        AuthorDao authorDao = manager.getAuthorDao();
        for (LoanDto loanDto : loanDtos) {
            long bookId = loanDto.getBook().getId();
            loanDto.setAuthors(authorDao.getByBookId(bookId));
        }
    }

    /**
     * Adds book quantity to each {@code LoanDto} in the given list
     */
    void addBookQuantityToLoanDtoInList(DaoManager manager, List<LoanDto> loanDtos) throws SQLException {
        LocationDao locationDao = manager.getLocationDao();
        for (LoanDto loanDto : loanDtos) {
            long bookId = loanDto.getBook().getId();
            loanDto.setBookQuantity(locationDao.getBookQuantity(bookId));
        }
    }

}
