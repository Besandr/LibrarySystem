package com.library.model.data.dao;

import com.library.model.data.entity.User;

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
}
