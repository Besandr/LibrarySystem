package com.library.repository.dao;

import java.util.List;
import java.util.Optional;

/**
 * Interface holds basic CRUD operations.
 * @param <T> - entity
 */
public interface Dao<T> {

    /**
     * Finds entity in a DB by its id in a table,
     * creates an entity object and return it.
     * @param id - entity id in the DB
     * @return Optional with entity or empty Optional
     */
    Optional<T> get(long id);

    /**
     * Finds all entities of type {@code T} in a
     * particular table and returns them.
     * @return List with all entities of type {@code T}
     */
    List<T> getAll();

    /**
     * Save an entity in a DB and returns id received
     * @param t - entity object
     * @return received id
     */
    long save(T t);

    /**
     * Changes given entity data in a DB table
     * @param t - entity with new values
     */
    void update(T t);

    /**
     * Deletes the row from DB table which represents given entity
     * @param t - entity which need to be deleted from DB
     */
    void delete(T t);

}
