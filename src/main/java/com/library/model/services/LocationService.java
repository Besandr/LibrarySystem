package com.library.model.services;

import com.library.model.data.DaoManager;
import com.library.model.data.DaoManagerFactory;
import com.library.model.data.dao.BookcaseDao;
import com.library.model.data.dao.LoanDtoDao;
import com.library.model.data.dao.LocationDao;
import com.library.model.data.entity.Book;
import com.library.model.data.entity.Bookcase;
import com.library.model.data.entity.Location;

import java.sql.SQLException;
import java.util.List;

/**
 * Service class which has methods bound with location operations
 */
public class LocationService extends Service {

    private static final LocationService instance = new LocationService();

    /**
     * Adds a new bookcase to the library storage and creates free locations
     * provided by it
     * @param bookcase - new library's bookcase
     * @return - boolean representation of result of method executing
     */
    public boolean addBookcaseToStorage(Bookcase bookcase) {

        DaoManager daoManager = DaoManagerFactory.createDaoManager();

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

        DaoManager daoManager = DaoManagerFactory.createDaoManager();

        Object executingResult = daoManager.executeTransaction(manager -> addBooksToStorageCommand(manager, bookId, booksQuantity));

        return checkAndCastExecutingResult(executingResult);
    }

    /**
     * Removes the given book from the storage
     * @param book - id of book which is needed to be removed
     * @return - boolean representation of result of method executing
     */
    public boolean removeBookFromStorage(Book book) {

        DaoManager daoManager = DaoManagerFactory.createDaoManager();

        Object executingResult = daoManager.executeTransaction(manager -> removeBookFromStorageCommand(manager, book));

        return checkAndCastExecutingResult(executingResult);
    }

    public static LocationService getInstance() {
        return instance;
    }

    //Commands which is needed to be executed in corresponding public service methods
    boolean addBookcaseToStorageCommand(DaoManager manager, Bookcase bookcase) throws SQLException {
        //saving the bookcase
        BookcaseDao bookcaseDao =(BookcaseDao) manager.getBookcaseDao();
        long bookcaseId = bookcaseDao.save(bookcase);
        bookcase.setId(bookcaseId);

        //creating new locations provided by the new bookcase
        LocationDao locationDao = (LocationDao) manager.getLocationDao();
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
        LocationDao locationDao = (LocationDao) manager.getLocationDao();
        List<Location> locations = locationDao.getAllLocations(true);

        if (locations.size() < booksQuantity) {
            return EXECUTING_FAILED;
        }

        for (int i = 0; i < booksQuantity; i++) {
            locationDao.saveBookToLocation(locations.get(i).getId(), bookId);
        }

        return EXECUTING_SUCCESSFUL;
    }

    private boolean removeBookFromStorageCommand(DaoManager manager, Book book) throws SQLException {

        LoanDtoDao loanDtoDao = manager.getLoanDtoDao();
        if (!loanDtoDao.getActiveLoansByBook(book).isEmpty()) {
            return EXECUTING_FAILED;
        }

        LocationDao locationDao = (LocationDao) manager.getLocationDao();
        locationDao.deleteBookFromAllLocations(book);

        return EXECUTING_SUCCESSFUL;
    }

    private LocationService(){}

}
