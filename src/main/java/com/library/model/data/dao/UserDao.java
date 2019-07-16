package com.library.model.data.dao;

import com.library.model.data.entity.User;

public interface UserDao extends Dao<User> {

    void updateKarma(long userId, int karma);

    void updateRole(long userId, long roleId);
}
