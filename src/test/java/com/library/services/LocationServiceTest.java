package com.library.services;

import com.library.repository.DaoManager;
import com.library.repository.DaoManagerFactory;
import com.library.repository.dao.BookcaseDao;
import com.library.repository.dao.LocationDao;
import com.library.repository.entity.Bookcase;
import com.library.repository.entity.Location;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LocationServiceTest {

    @Mock
    DaoManager mockDaoManager;

    @Mock
    LocationDao mockLocationDao;

    @Mock
    BookcaseDao mockBookcaseDao;

    @Mock
    Location mockLocation;

    @Mock
    Bookcase mockBookcase;

    @Mock
    LocationService mockService;

    @Before
    public void initSetUp() throws SQLException {
        mockService = spy(new LocationService(new DaoManagerFactory()));
        when(mockDaoManager.getLocationDao()).thenReturn(mockLocationDao);
        when(mockDaoManager.getBookcaseDao()).thenReturn(mockBookcaseDao);
    }

    @Test
    public void addBooksToStorageCommandShouldReturnTrue() throws Exception {

        int booksQuantity = 10;
        long bookId = 3;

        List<Location> locations = new ArrayList<>();
        for (int i = 0; i < booksQuantity; i++) locations.add(mockLocation);

        when(mockLocationDao.getAllLocations(true)).thenReturn(locations);

        boolean result = mockService.addBooksToStorageCommand(mockDaoManager, bookId, booksQuantity);

        verify(mockLocationDao, times(booksQuantity)).saveBookToLocation(anyLong(),eq(bookId));
        assertTrue(result);
    }

    @Test
    public void addBooksToStorageCommandShouldReturnFalse() throws Exception {

        int booksQuantity = 10;
        long bookId = 3;

        List<Location> locations = new ArrayList<>();
        //There are not enough free cells for adding book
        for (int i = 0; i < booksQuantity - 2; i++) locations.add(mockLocation);

        when(mockLocationDao.getAllLocations(true)).thenReturn(locations);

        boolean result = mockService.addBooksToStorageCommand(mockDaoManager, bookId, booksQuantity);

        verify(mockLocationDao, times(0)).saveBookToLocation(anyLong(),eq(bookId));
        assertFalse(result);
    }

    @Test
    public void addBookcaseToStorageCommandShouldCallSaveLocation20times() throws Exception {

        Bookcase bookcase = Bookcase.builder().shelfQuantity(2).cellQuantity(10).build();

        mockService.addBookcaseToStorageCommand(mockDaoManager, bookcase);

        verify(mockBookcaseDao, times(1)).save(bookcase);
        verify(mockLocationDao, times(20)).save(any());
    }
}