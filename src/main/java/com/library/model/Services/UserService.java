package com.library.model.Services;

import com.library.model.data.DaoManager;
import com.library.model.data.DaoManagerFactory;
import com.library.model.data.dao.UserDao;
import com.library.model.data.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class UserService {

    private static final Logger log = LogManager.getLogger(UserService.class);

    public static final UserService instance = new UserService();

    public long createNewUser(User user) {

        user.setPassword(hashPassword(user.getPassword()));

        DaoManager daoManager = DaoManagerFactory.createDaoManager();

        return (Long) daoManager.executeTransaction( manager -> createNewUserCommand(manager, user));
    }

    protected long createNewUserCommand(DaoManager manager, User user) throws SQLException {

        UserDao userDao = (UserDao) manager.getUserDao();
        return userDao.save(user);

    }

    protected String hashPassword(String passwordToHash) {

        final String salt = "nox#!9Z7";

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            log.error("Password hasher can't find hash algorithm");
        }
        md.update(salt.getBytes());

        byte[] hashedPassword = md.digest(passwordToHash.getBytes(StandardCharsets.UTF_8));

        return new String(hashedPassword);
    }

    public static UserService getInstance() {
        return instance;
    }

    private UserService(){}
}
