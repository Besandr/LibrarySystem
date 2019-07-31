package com.library.services;

import com.library.repository.DaoManager;
import com.library.repository.DaoManagerFactory;
import com.library.repository.dao.interfaces.UserDao;
import com.library.repository.entity.Role;
import com.library.repository.entity.User;
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

    private DaoManagerFactory daoManagerFactory;

    UserService(DaoManagerFactory daoManagerFactory) {
        this.daoManagerFactory = daoManagerFactory;
    }

    /**
     * Creates(saves) a new user.
     * @param firstName - the user's first name
     * @param lastName - the user's last name
     * @param email - the user's e-mail
     * @param phone - the user's phone number
     * @param password - the user's password
     * @return an {@code Optional} with created user if saving
     * was successful or an empty {@code Optional} if it wasn't
     */
    public Optional<User> createNewUser(String firstName,
                                 String lastName,
                                 String email,
                                 String phone,
                                 String password) {

        User user = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phone(phone)
                .role(Role.USER)
                .karma(3)
                .build();

        user.setPassword(hashPassword(password));

        DaoManager daoManager = daoManagerFactory.createDaoManager();

        Object executingResult = daoManager.executeTransaction(manager -> createNewUserCommand(manager, user));

        if (checkAndCastExecutingResult(executingResult)) {
            //Erasing unnecessary for caller password
            user.setPassword("");
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
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

        DaoManager daoManager = daoManagerFactory.createDaoManager();

        Object executingResult = daoManager.executeAndClose(manager -> getUserByLoginInfoCommand(manager, email, hashedPassword));

        return checkAndCastObjectToOptional(executingResult);
    }

    /**
     * Replaces old user's personal repository to the repository from given user
     * @param user - object with a new user's repository
     * @return - boolean result of method executing
     */
    public boolean updateUsersProfile(User user) {

        DaoManager daoManager = daoManagerFactory.createDaoManager();

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

        return new String(hashedPassword, StandardCharsets.UTF_8);
    }

}
