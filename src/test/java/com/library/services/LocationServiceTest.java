package com.library.services;

import com.library.repository.DaoManager;
import com.library.repository.DaoManagerFactory;
import com.library.repository.dao.BookcaseDao;
import com.library.repository.dao.LoanDao;
import com.library.repository.dao.LoanDtoDao;
import com.library.repository.dao.LocationDao;
import com.library.repository.dto.LoanDto;
import com.library.repository.entity.Book;
import com.library.repository.entity.Bookcase;
import com.library.repository.entity.Loan;
import com.library.repository.entity.Location;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.SQLException;
import java.util.*;

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
    LoanDtoDao mockLoanDtoDao;

    @Mock
    LoanDao mockLoanDao;

    @Mock
    Location mockLocation;

    @Mock
    LocationService mockService;

    private long TEST_BOOK_ID = 3L;

    @Before
    public void initSetUp() throws SQLException {
        mockService = spy(new LocationService(new DaoManagerFactory()));
        when(mockDaoManager.getLocationDao()).thenReturn(mockLocationDao);
        when(mockDaoManager.getBookcaseDao()).thenReturn(mockBookcaseDao);
        when(mockDaoManager.getLoanDtoDao()).thenReturn(mockLoanDtoDao);
        when(mockDaoManager.getLoanDao()).thenReturn(mockLoanDao);
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

    @Test
    public void removeBookFromStorageCommandShouldCallDeletingMethodsAndReturnTrue() throws SQLException {
        long TEST_LOAN_ID = 5L;
        LoanDto dto = LoanDto.builder()
                .book(Book.builder().id(TEST_BOOK_ID).build())
                .loan(Loan.builder().id(TEST_LOAN_ID).build())
                .build();
        List<LoanDto> loanDtoList = Arrays.asList(dto, dto, dto);

        when(mockLoanDtoDao.getActiveLoansByBookId(TEST_BOOK_ID)).thenReturn(Collections.emptyList());
        when(mockLoanDtoDao.getUnapprovedLoansByBookId(TEST_BOOK_ID)).thenReturn(loanDtoList);

        boolean result = mockService.removeBookFromStorageCommand(mockDaoManager, TEST_BOOK_ID);

        verify(mockLoanDao, times(3)).delete(dto.getLoan());
        verify(mockLocationDao).deleteBookFromAllLocations(TEST_BOOK_ID);
        assertTrue(result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void removeBookFromStorageCommandShouldDoesNotCallDeletingMethodsAndReturnFalse() throws SQLException {
        List mockList = mock(List.class);
        when(mockList.isEmpty()).thenReturn(false);
        when(mockLoanDtoDao.getActiveLoansByBookId(TEST_BOOK_ID)).thenReturn(mockList);

        boolean result = mockService.removeBookFromStorageCommand(mockDaoManager, TEST_BOOK_ID);

        verify(mockLoanDao, never()).delete(any(Loan.class));
        verify(mockLocationDao, never()).deleteBookFromAllLocations(anyLong());
        assertFalse(result);
    }
}