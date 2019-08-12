package com.library.services;

import com.library.repository.DaoManager;
import com.library.repository.DaoManagerFactory;
import com.library.repository.dao.UserDao;
import com.library.repository.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    UserService mockService;

    @Mock
    DaoManager mockDaoManager;

    @Mock
    UserDao mockUserDao;

    @Mock
    User mockUser;

    @Before
    public void initSetUp() throws SQLException {
        mockService = spy(new UserService(new DaoManagerFactory()));
        when(mockDaoManager.getUserDao()).thenReturn(mockUserDao);
    }

    @Test
    public void createNewUserCommandShouldSaveUserAndReturnTrue() throws SQLException {
        final long TEST_USER_ID = 3L;
        when(mockUserDao.save(any(User.class))).thenReturn(TEST_USER_ID);

        boolean result = mockService.createNewUserCommand(mockDaoManager, mockUser);
        verify(mockUserDao).save(mockUser);
        assertTrue(result);
    }

    @Test
    public void createNewUserCommandShouldSaveUserAndReturnFalse() throws SQLException {
        final long TEST_USER_ID = -1L;
        when(mockUserDao.save(any(User.class))).thenReturn(TEST_USER_ID);

        boolean result = mockService.createNewUserCommand(mockDaoManager, mockUser);
        verify(mockUserDao).save(mockUser);
        assertFalse(result);
    }

    @Test
    public void testHashPassword(){

        UserService service = new UserService(new DaoManagerFactory());

        String password = "Hello";
        String hashedPassword = service.hashPassword(password);
        assertNotEquals("Password and hashed password must not be equal", password, hashedPassword);

        String hashedPassword2 = service.hashPassword(password);
        assertEquals("hashPassword method must return equals hashes for equals passwords", hashedPassword, hashedPassword2);

        String anotherPassword = "World";
        String anotherHash = service.hashPassword(anotherPassword);
        assertNotEquals("hashPassword method must return different hashes for different passwords", hashedPassword, anotherHash);
    }
}