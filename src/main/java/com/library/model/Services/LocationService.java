package com.library.model.Services;

import com.library.model.data.DaoManager;
import com.library.model.data.DaoManagerFactory;
import com.library.model.data.dao.BookcaseDao;
import com.library.model.data.dao.LocationDao;
import com.library.model.data.entity.Bookcase;
import com.library.model.data.entity.Location;

import java.sql.SQLException;
import java.util.List;

public class LocationService {

    private static final LocationService instance = new LocationService();

    public boolean addBookcaseToStorage(Bookcase bookcase) {

        DaoManager daoManager = DaoManagerFactory.createDaoManager();

        return (Boolean) daoManager.executeTransaction(manager -> addBookcaseToStorageCommand(manager, bookcase));
    }

    public boolean addBooksToStorage(long bookId, int booksQuantity) {

        DaoManager daoManager = DaoManagerFactory.createDaoManager();

        return (Boolean) daoManager.executeTransaction( manager -> addBooksToStorageCommand(manager, bookId, booksQuantity));
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
    private LocationService(){}

    public static void main(String[] args) {

//        Bookcase bookcase = Bookcase.builder()
//                .cellQuantity(10)
//                .shelfQuantity(8)
//                .build();
//        System.out.println(LocationService.getInstance().addBookcaseToStorage(bookcase));

//        System.out.println(LocationService.getInstance().addBooksToStorage(6, 10));
    }

}
