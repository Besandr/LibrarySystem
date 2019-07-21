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
import java.util.Optional;

/**
 * Service class which has methods bound with user operations
 */
public class UserService extends Service{

    private static final Logger log = LogManager.getLogger(UserService.class);

    public static final UserService instance = new UserService();

    /**
     * Creates(saves) a new user.
     * @param user - user which is need to be saved
     * @return {@code true} if saving is successful
     *         and {@code false} if it is not
     */
    public boolean createNewUser(User user) {

        user.setPassword(hashPassword(user.getPassword()));

        DaoManager daoManager = DaoManagerFactory.createDaoManager();

        Object executingResult = daoManager.executeTransaction(manager -> createNewUserCommand(manager, user));

        return checkAndCastExecutingResult(executingResult);
    }

    /**
     * Gets an {@code Optional} of user with given combination of email & password
     * @param email - an user's email
     * @param password - a user's password
     * @return - an {@code Optional} of target user or an empty {@code Optional}
     *          if there is no user with given combination of email & password
     */
    public Optional<User> getUserByLoginInfo(String email, String password) {
        //The passwords stored in their hashed version so we need
        // to get hash of the password first
        String hashedPassword = hashPassword(password);

        DaoManager daoManager = DaoManagerFactory.createDaoManager();

        Object executingResult = daoManager.executeAndClose(manager -> getUserByLoginInfoCommand(manager, email, hashedPassword));

        return checkAndCastObjectToOptional(executingResult);
    }

    /**
     * Replaces old user's personal data to the data from given user
     * @param user - object with a new user's data
     * @return - boolean result of method executing
     */
    public boolean updateUsersProfile(User user) {

        DaoManager daoManager = DaoManagerFactory.createDaoManager();

        Object executingResult = daoManager.executeAndClose(manager -> updateUsersProfileCommand(manager, user));

        return checkAndCastExecutingResult(executingResult);
    }

    //Commands which is needed to be executed in corresponding public service methods
    boolean createNewUserCommand(DaoManager manager, User user) throws SQLException {

        UserDao userDao = (UserDao) manager.getUserDao();
        long id = userDao.save(user);
        if (id > 0) {
            return EXECUTING_SUCCESSFUL;
        } else {
            return EXECUTING_FAILED;
        }
    }

    Optional<User> getUserByLoginInfoCommand(DaoManager manager, String email, String password) throws SQLException {

        UserDao dao = (UserDao) manager.getUserDao();

        return dao.getUserByEmailAndPassword(email, password);
    }

    boolean updateUsersProfileCommand(DaoManager manager, User user) throws SQLException {

        UserDao userDao = (UserDao) manager.getUserDao();

        userDao.update(user);

        return EXECUTING_SUCCESSFUL;
    }

    /**
     * Makes the hashed password for storing hash of the password instead
     * of it's raw version
     * @param passwordToHash - password which is need to be hashed
     * @return - hashed version of password
     */
    String hashPassword(String passwordToHash) {

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
