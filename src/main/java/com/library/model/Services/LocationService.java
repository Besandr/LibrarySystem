package com.library.model.Services;

import com.library.model.data.DaoManager;
import com.library.model.data.DaoManagerFactory;
import com.library.model.data.dao.BookcaseDao;
import com.library.model.data.dao.LocationDao;
import com.library.model.data.entity.Bookcase;
import com.library.model.data.entity.Location;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class LocationService {

    private static final LocationService instance = new LocationService();

    public boolean addBookcaseToStorage(Bookcase bookcase) {

        DaoManager daoManager = DaoManagerFactory.createDaoManager();

        Object executingResult = daoManager.executeTransaction(manager -> addBookcaseToStorageCommand(manager, bookcase));

        return checkAndCastExecutingResult(executingResult);
    }

    public boolean addBooksToStorage(long bookId, int booksQuantity) {

        DaoManager daoManager = DaoManagerFactory.createDaoManager();

        Object executingResult = daoManager.executeTransaction(manager -> addBooksToStorageCommand(manager, bookId, booksQuantity));

        return checkAndCastExecutingResult(executingResult);
    }

    public static LocationService getInstance() {
        return instance;
    }

    protected boolean addBooksToStorageCommand(DaoManager manager, long bookId, int booksQuantity) throws SQLException {
        LocationDao locationDao = (LocationDao) manager.getLocationDao();
        List<Location> locations = locationDao.getAllLocations(true);

        if (locations.size() < booksQuantity) {
            return false;
        }

        for (int i = 0; i < booksQuantity; i++) {
            locationDao.saveBookToLocation(locations.get(i).getId(), bookId);
        }

        return true;
    }

    protected boolean addBookcaseToStorageCommand(DaoManager manager, Bookcase bookcase) throws SQLException {
        BookcaseDao bookcaseDao =(BookcaseDao) manager.getBookcaseDao();
        long bookcaseId = bookcaseDao.save(bookcase);
        bookcase.setId(bookcaseId);

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
        return true;
    }

    protected boolean checkAndCastExecutingResult(Object executingResult) {
        if (Objects.nonNull(executingResult) && executingResult instanceof Boolean) {
            return (Boolean) executingResult;
        } else {
            return false;
        }
    }

    private LocationService(){}

}
