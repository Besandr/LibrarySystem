package com.library.repository.dao.impl;

import com.library.repository.DBUtils;
import com.library.repository.dao.LocationDao;
import com.library.repository.entity.Location;
import com.library.repository.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementing of LocationDao for working with a SQL server
 */
public class LocationDaoImpl implements LocationDao {

    private static final Logger log = LogManager.getLogger(LocationDao.class);

    private Connection connection;

    public LocationDaoImpl(Connection connection) {
        this.connection = connection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Location> get(long locationId) {

        Optional<Location> resultOptional = Optional.empty();

        try {
            PreparedStatement getLocationStatement = connection
                    .prepareStatement(DBQueries.GET_LOCATION_QUERY);
            getLocationStatement.setLong(1, locationId);

            ResultSet rs = getLocationStatement.executeQuery();

            if (rs.next()) {
                resultOptional = Optional.of(getLocationFromResultRow(rs));
            }

            rs.close();

        } catch (SQLException e) {
            String errorText = String.format("Can't get location by id: %s. Cause: %s", locationId, e.getMessage());
            log.error(errorText, e);
            throw new DaoException(errorText, e);
        }

        return resultOptional;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Location> getAll() {
        return getAllLocations(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long save(Location location) {

        try {
            PreparedStatement insertStatement = connection
                    .prepareStatement(DBQueries.SAVE_LOCATION_QUERY, Statement.RETURN_GENERATED_KEYS);
            insertStatement.setLong(1, location.getBookcaseId());
            insertStatement.setInt(2, location.getShelfNumber());
            insertStatement.setInt(3, location.getCellNumber());

            insertStatement.executeUpdate();

            return DBUtils.getIdFromStatement(insertStatement);

        } catch (SQLException e) {
            if (DBUtils.isTryingToInsertDuplicate(e)) {
                return -1;
            } else {
                String errorText = String.format("Can't save locastion: %s. Cause: %s", location, e.getMessage());
                log.error(errorText, e);
                throw new DaoException(errorText, e);
            }
        }
    }

    /**
     * Universal update is not supported. Use special update
     * for special cases
     */
    @Override
    public void update(Location location) {
        throw new UnsupportedOperationException("Universal update is not supported. Use special update for special cases");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveBookToLocation(long locationId, long bookId) {

        try {
            PreparedStatement updateStatement = connection.prepareStatement(DBQueries.SAVE_BOOK_TO_LOCATION_QUERY);
            updateStatement.setLong(1, bookId);
            updateStatement.setLong(2, locationId);

            updateStatement.execute();

        } catch (SQLException e) {
            String errorText = String.format("Can't update book_id in Location table. " +
                    "Location_id: %s. Cause: %s", locationId, e.getMessage());
            log.error(errorText, e);
            throw new DaoException(errorText, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteBookFromAllLocations(long bookId) {

        try{
            PreparedStatement statement = connection.prepareStatement(DBQueries.DELETE_BOOK_FROM_ALL_LOCATIONS_QUERY);
            statement.setLong(1, bookId);

            statement.execute();

        } catch (SQLException e) {
            String errorText = String.format("Can't delete book from all locations. BookId: %s. Cause: %s", bookId, e.getMessage());
            log.error(errorText, e);
            throw new DaoException(errorText, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateIsOccupied(long locationId, boolean isOccupied) {

        try {

            PreparedStatement updateStatement = connection.prepareStatement(DBQueries.UPDATE_LOCATION_OCCUPIED_QUERY);
            updateStatement.setBoolean(1, isOccupied);
            updateStatement.setLong(2, locationId);

            updateStatement.execute();

        } catch (SQLException e) {
            String errorText = String.format("Can't update is_occupied in Location table. " +
                    "Location_id: %s. Cause: %s", locationId, e.getMessage());
            log.error(errorText, e);
            throw new DaoException(errorText, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Location location) {

        try {
            PreparedStatement deleteStatement = connection
                    .prepareStatement(DBQueries.DELETE_LOCATION_QUERY);
            deleteStatement.setLong(1, location.getId());

            deleteStatement.execute();

        } catch (SQLException e) {
            String errorText = String.format("Can't delete location: %s. Cause: %s", location, e.getMessage());
            log.error(errorText, e);
            throw new DaoException(errorText, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getBookQuantity(long book_id) {

        try {
            PreparedStatement getStatement = connection.prepareStatement(DBQueries.GET_BOOK_QUANTITY_QUERY);
            getStatement.setLong(1, book_id);

            ResultSet rs = getStatement.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return 0;
            }

        } catch (SQLException e) {
            String errorText = String.format("Can't get book quantity for book: %s. Cause: %s", book_id, e.getMessage());
            log.error(errorText, e);
            throw new DaoException(errorText, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Location> getAllLocations(boolean onlyFreeLocations) {

        List<Location> locations = new ArrayList<>();

        try {
            PreparedStatement selectStatement;

            if (onlyFreeLocations) {
                selectStatement = connection.prepareStatement(DBQueries.GET_FREE_LOCATIONS_QUERY);
            } else {
                selectStatement = connection.prepareStatement(DBQueries.ALL_LOCATIONS_QUERY);
            }


            ResultSet rs = selectStatement.executeQuery();

            while (rs.next()) {
                locations.add(getLocationFromResultRow(rs));
            }

            rs.close();

        } catch (SQLException e) {
            String errorText = "Can't get locations list from DB. Cause: " + e.getMessage();
            log.error(errorText, e);
            throw new DaoException(errorText, e);
        }

        return locations;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Location> getBookLocation(long book_id, boolean isOccupied) {

        Optional<Location> resultOptional = Optional.empty();

        try {
            PreparedStatement findLocationStatement = connection.prepareStatement(DBQueries.GET_BOOK_LOCATION_QUERY);
            findLocationStatement.setLong(1, book_id);
            findLocationStatement.setBoolean(2, isOccupied);

            ResultSet rs = findLocationStatement.executeQuery();

            if (rs.next()) {
                resultOptional = Optional.of(getLocationFromResultRow(rs));
            }

        } catch (SQLException e) {
            String errorText = String.format("Can't get book location for book: %s. Cause: %s", book_id, e.getMessage());
            log.error(errorText, e);
            throw new DaoException(errorText, e);
        }

        return resultOptional;
    }

    private Location getLocationFromResultRow(ResultSet rs) throws SQLException {

        return Location.builder()
                .id(rs.getLong("location_id"))
                .bookcaseId(rs.getLong("bookcase_id"))
                .shelfNumber(rs.getInt("shelf_number"))
                .cellNumber(rs.getInt("cell_number"))
                .bookId(rs.getLong("book_id"))
                .isOccupied(rs.getBoolean("is_occupied"))
                .build();
    }
}
