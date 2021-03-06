package com.library.services;

import com.library.repository.DaoManager;
import com.library.repository.DaoManagerFactory;
import com.library.repository.dao.BookcaseDao;
import com.library.repository.dao.LoanDao;
import com.library.repository.dao.LoanDtoDao;
import com.library.repository.dao.LocationDao;
import com.library.repository.dto.LoanDto;
import com.library.repository.entity.Bookcase;
import com.library.repository.entity.Location;

import java.sql.SQLException;
import java.util.List;

/**
 * Service class which has methods bound with location operations
 * and DAO
 */
public class LocationService extends Service {

    private DaoManagerFactory daoManagerFactory;

    LocationService(DaoManagerFactory daoManagerFactory) {
        this.daoManagerFactory = daoManagerFactory;
    }

    /**
     * Adds a new bookcase to the library storage and creates free locations
     * provided by it
     * @param shelfQuantity - quantity of shelves of the new bookcase
     * @param cellsQuantity - quantity of book cells on the shelves
     * @return - boolean representation of result of method executing
     */
    public boolean addBookcaseToStorage(int shelfQuantity, int cellsQuantity) {

        Bookcase bookcase = Bookcase.builder()
                .shelfQuantity(shelfQuantity)
                .cellQuantity(cellsQuantity)
                .build();

        DaoManager daoManager = daoManagerFactory.createDaoManager();

        Object executingResult = daoManager.executeTransaction(manager -> addBookcaseToStorageCommand(manager, bookcase));

        return checkAndCastExecutingResult(executingResult);
    }

    /**
     * Adds given quantity of books to storage. Added books occupy free
     * locations in the storage
     * @param bookId - id of book which is needed to be added
     * @param booksQuantity - quantity of book which is needed to be added
     * @return - boolean representation of result of method executing
     */
    public boolean addBooksToStorage(long bookId, int booksQuantity) {

        DaoManager daoManager = daoManagerFactory.createDaoManager();

        Object executingResult = daoManager.executeTransaction(manager -> addBooksToStorageCommand(manager, bookId, booksQuantity));

        return checkAndCastExecutingResult(executingResult);
    }

    /**
     * Removes the given book from the storage
     * @param bookId - id of book which is needed to be removed
     * @return - boolean representation of result of method executing
     */
    public boolean removeBookFromStorage(long bookId) {

        DaoManager daoManager = daoManagerFactory.createDaoManager();

        Object executingResult = daoManager.executeTransaction(manager -> removeBookFromStorageCommand(manager, bookId));

        return checkAndCastExecutingResult(executingResult);
    }

    //Commands which is needed to be executed in corresponding public service methods
    boolean addBookcaseToStorageCommand(DaoManager manager, Bookcase bookcase) throws SQLException {
        //saving the bookcase
        BookcaseDao bookcaseDao = manager.getBookcaseDao();
        long bookcaseId = bookcaseDao.save(bookcase);
        bookcase.setId(bookcaseId);

        //creating new locations provided by the new bookcase
        LocationDao locationDao = manager.getLocationDao();
        for (int i = 1; i <= bookcase.getShelfQuantity(); i++) {
            for (int j = 1; j <= bookcase.getCellQuantity(); j++) {
                Location location = Location.builder()
                        .bookcaseId(bookcaseId)
                        .shelfNumber(i)
                        .cellNumber(j)
                        .build();
                locationDao.save(location);
            }
        }
        return EXECUTING_SUCCESSFUL;
    }

    boolean addBooksToStorageCommand(DaoManager manager, long bookId, int booksQuantity) throws SQLException {
        LocationDao locationDao = manager.getLocationDao();
        List<Location> locations = locationDao.getAllLocations(true);

        if (locations.size() < booksQuantity) {
            return EXECUTING_FAILED;
        }

        for (int i = 0; i < booksQuantity; i++) {
            locationDao.saveBookToLocation(locations.get(i).getId(), bookId);
        }

        return EXECUTING_SUCCESSFUL;
    }

    synchronized boolean removeBookFromStorageCommand(DaoManager manager, long bookId) throws SQLException {

        // Checking for not returned books
        LoanDtoDao loanDtoDao = manager.getLoanDtoDao();
        if (!loanDtoDao.getActiveLoansByBookId(bookId).isEmpty()) {
            return EXECUTING_FAILED;
        }

        // Removing unapproved loans for the book being deleted
        List<LoanDto> unapprovedLoans = loanDtoDao.getUnapprovedLoansByBookId(bookId);
        LoanDao loanDao = manager.getLoanDao();
        for (LoanDto loanDto : unapprovedLoans) {
            loanDao.delete(loanDto.getLoan());
        }

        // Removing book from the storage
        LocationDao locationDao = manager.getLocationDao();
        locationDao.deleteBookFromAllLocations(bookId);

        return EXECUTING_SUCCESSFUL;
    }

}
