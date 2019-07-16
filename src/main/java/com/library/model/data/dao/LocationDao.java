package com.library.model.data.dao;

import com.library.model.data.entity.Location;

import java.util.List;
import java.util.Optional;

public interface LocationDao extends Dao<Location> {

    void saveBookToLocation(long locationId, long bookId);

    void updateIsOccupied(long locationId, boolean isOccupied);

    int getBookQuantity(long book_id);

    /**
     * Gets list of all locations of of free locations (represents free
     * cells in a library storage).
     *
     * @param onlyFreeLocations true if need only free locations and false
     *                          if need all the locations
     * @return list of all locations or all free locations depending on
     *         given argument
     */
    List<Location> getAllLocations(boolean onlyFreeLocations);

    Optional<Location> getBookLocation(long book_id, boolean isOccupied);
}
