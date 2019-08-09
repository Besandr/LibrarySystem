package com.library.repository.dao;

import com.library.repository.entity.Book;
import com.library.repository.entity.Location;

import java.util.List;
import java.util.Optional;

/**
 * Dao for Locations entity
 */
public interface LocationDao extends Dao<Location> {

    /**
     * Saves book to target(free) location.
     * @param locationId - id of free location
     * @param bookId - id of book to be saved
     */
    void saveBookToLocation(long locationId, long bookId);

    /**
     * Deletes information of given book in every location occupied by it
     * @param bookId - ID of book for deleting.
     */
    void deleteBookFromAllLocations(long bookId);

    /**
     * Changes is_occupied state of given location
     * @param locationId - id of location to be changed
     * @param isOccupied - new status of location
     *                   false - for taking a book from location to user
     *                   true - for returning a book to location
     */
    void updateIsOccupied(long locationId, boolean isOccupied);

    /**
     * Gets the quantity of given id books in the storage (present and
     * borrowed)
     * @param book_id - id of book which locations need to be counted
     * @return - quantity of target books in the storage(present and
     * borrowed)
     */
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
