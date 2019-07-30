package com.library.repository.dao;

import com.library.repository.entity.User;

import java.util.Optional;

/**
 * Dao for Books entity
 */
public interface UserDao extends Dao<User> {

    /**
     * Changes the user's. Karma can be decreased
     * as well as increased depending on delta value.
     * @param userId - id of user whose karma is needed
     *               to be changed
     * @param delta - the value which is added to user's
     *              karma. Can be negative and positive
     */
    void updateKarma(long userId, int delta);

    /**
     * Changes the user's role in the application
     * @param userId - id of user whose role is needed
     *                 to be changed
     * @param roleId - the id of new user's role
     */
    void updateRole(long userId, long roleId);

    /**
     * Gets an {@code Optional} with user by its email & password
     * @param email - email of a target user
     * @param password - password of a target use
     * @return
     */
    Optional<User> getUserByEmailAndPassword(String email, String password);
}
