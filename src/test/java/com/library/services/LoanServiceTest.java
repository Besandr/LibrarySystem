package com.library.services;

import com.library.repository.DaoManager;
import com.library.repository.DaoManagerFactory;
import com.library.repository.dao.*;
import com.library.repository.dto.LoanDto;
import com.library.repository.entity.Author;
import com.library.repository.entity.Book;
import com.library.repository.entity.Loan;
import com.library.repository.entity.Location;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LoanServiceTest {

    @Mock
    LoanService mockService;

    @Mock
    DaoManager mockManager;

    @Mock
    LoanDao mockLoanDao;

    @Mock
    UserDao mockUserDao;

    @Mock
    AuthorDao mockAuthorDao;

    @Mock
    LocationDao mockLocationDao;

    @Mock
    LoanDtoDao mockLoanDtoDao;

    private List<LoanDto> loanDtoList;
    private final long TEST_BOOK_ID = 3L;
    private final long TEST_LOAN_ID = 7L;
    private final long TEST_LOCATION_ID = 11L;
    private final long TEST_USER_ID = 13L;

    @Before
    public void initSetUp() throws SQLException {
        mockService = spy(new LoanService(new DaoManagerFactory()));
        when(mockManager.getLoanDao()).thenReturn(mockLoanDao);
        when(mockManager.getUserDao()).thenReturn(mockUserDao);
        when(mockManager.getAuthorDao()).thenReturn(mockAuthorDao);
        when(mockManager.getLocationDao()).thenReturn(mockLocationDao);
        when(mockManager.getLoanDtoDao()).thenReturn(mockLoanDtoDao);

        LoanDto loanDto = LoanDto.builder()
                .book(Book.builder().id(3L).build())
                .build();
        loanDtoList = new ArrayList<>(Arrays.asList(loanDto, loanDto, loanDto));
    }

    @Test
    public void getAllUnapprovedLoansCommandShouldAddAuthorsAndBooksAndReturnCorrectDTOs() throws SQLException {
        when(mockLoanDtoDao.getUnapprovedLoans(10, 5)).thenReturn(loanDtoList);
        doNothing().when(mockService).addAuthorsToLoanDtoInList(any(), any());
        doNothing().when(mockService).addBookQuantityToLoanDtoInList(any(), any());

        List<LoanDto> result = mockService.getAllUnapprovedLoansCommand(mockManager, 10, 5);

        verify(mockService).addBookQuantityToLoanDtoInList(mockManager, loanDtoList);
        verify(mockService).addAuthorsToLoanDtoInList(mockManager, loanDtoList);
        assertEquals(loanDtoList, result);
    }

    @Test
    public void getUnapprovedLoansByUserCommandShouldAddAuthorsAndReturnCorrectDTOs() throws SQLException {
        when(mockLoanDtoDao.getUnapprovedLoansByUserId(TEST_USER_ID,10, 5)).thenReturn(loanDtoList);
        doNothing().when(mockService).addAuthorsToLoanDtoInList(any(), any());

        List<LoanDto> result = mockService.getUnapprovedLoansByUserCommand(mockManager, TEST_USER_ID, 10, 5);

        verify(mockService, times(1)).addAuthorsToLoanDtoInList(mockManager, loanDtoList);
        assertEquals(loanDtoList, result);
    }

    @Test
    public void getActiveLoansCommandShouldAddAuthorsAndReturnCorrectDTOs() throws SQLException {
        when(mockLoanDtoDao.getActiveLoans(10, 5)).thenReturn(loanDtoList);
        doNothing().when(mockService).addAuthorsToLoanDtoInList(any(), any());

        List<LoanDto> result = mockService.getActiveLoansCommand(mockManager, 10, 5);

        verify(mockService).addAuthorsToLoanDtoInList(mockManager, loanDtoList);
        assertEquals(loanDtoList, result);
    }

    @Test
    public void getActiveLoansByUserCommandShouldAddAuthorsAndReturnCorrectDTOs() throws SQLException {
        when(mockLoanDtoDao.getActiveLoansByUserId(TEST_USER_ID, 10, 5)).thenReturn(loanDtoList);
        doNothing().when(mockService).addAuthorsToLoanDtoInList(any(), any());

        List<LoanDto> result = mockService.getActiveLoansByUserCommand(mockManager, TEST_USER_ID, 10, 5);

        verify(mockService).addAuthorsToLoanDtoInList(mockManager, loanDtoList);
        assertEquals(loanDtoList, result);
    }

    @Test
    public void getReturnedLoansByUserCommanddShouldAddAuthorsAndReturnCorrectDTOs() throws SQLException {
        when(mockLoanDtoDao.getReturnedLoansByUserId(TEST_USER_ID, 10, 5)).thenReturn(loanDtoList);
        doNothing().when(mockService).addAuthorsToLoanDtoInList(any(), any());

        List<LoanDto> result = mockService.getReturnedLoansByUserCommand(mockManager, TEST_USER_ID, 10, 5);

        verify(mockService).addAuthorsToLoanDtoInList(mockManager, loanDtoList);
        assertEquals(loanDtoList, result);
    }

    @Test
    public void getActiveLoansByBookCommandShouldAddAuthorsAndBooksAndReturnCorrectDTOs() throws SQLException {
        when(mockLoanDtoDao.getActiveLoansByBookId(TEST_BOOK_ID)).thenReturn(loanDtoList);
        doNothing().when(mockService).addAuthorsToLoanDtoInList(any(), any());

        List<LoanDto> result = mockService.getActiveLoansByBookCommand(mockManager, TEST_BOOK_ID);

        verify(mockService).addAuthorsToLoanDtoInList(mockManager, loanDtoList);
        assertEquals(loanDtoList, result);
    }

    @Test
    public void approveLoanCommandShouldUpdateLoanAndBookLocationAndReturnTrue() throws SQLException {
        Loan loan = Loan.builder()
                .bookId(TEST_BOOK_ID)
                .build();
        when(mockLoanDao.get(TEST_LOAN_ID)).thenReturn(Optional.of(loan));

        Location location = Location.builder().id(TEST_LOCATION_ID).build();
        when(mockLocationDao.getBookLocation(TEST_BOOK_ID, true)).thenReturn(Optional.of(location));

        boolean result = mockService.approveLoanCommand(mockManager, TEST_LOAN_ID);

        verify(mockLoanDao).updateLoanAndExpiredDate(TEST_LOAN_ID, LocalDate.now(), LocalDate.now().plusMonths(1));
        verify(mockLocationDao).updateIsOccupied(TEST_LOCATION_ID, false);
        assertTrue(result);
    }

    @Test
    public void approveLoanCommandShouldReturnFalseWithEmptyLoanOptional() throws SQLException {
        boolean result = mockService.approveLoanCommand(mockManager, TEST_LOAN_ID);

        verify(mockLoanDao, never()).updateLoanAndExpiredDate(anyLong(), any(LocalDate.class), any(LocalDate.class));
        verify(mockLocationDao, never()).getBookLocation(anyLong(), anyBoolean());
        assertFalse(result);
    }

    @Test
    public void approveLoanCommandShouldReturnFalseWithEmptyLocationOptional() throws SQLException {
        Loan loan = Loan.builder()
                .bookId(TEST_BOOK_ID)
                .build();
        when(mockLoanDao.get(TEST_LOAN_ID)).thenReturn(Optional.of(loan));

        boolean result = mockService.approveLoanCommand(mockManager, TEST_LOAN_ID);

        verify(mockLoanDao, never()).updateLoanAndExpiredDate(anyLong(), any(LocalDate.class), any(LocalDate.class));
        verify(mockLocationDao, never()).updateIsOccupied(anyLong(), anyBoolean());
        assertFalse(result);
    }

    @Test
    public void returnBookCommandShouldUpdateLocationAndReturnDateAndKarma() throws SQLException {
        Loan loan = Loan.builder()
                .bookId(TEST_BOOK_ID)
                .build();
        when(mockLoanDao.get(TEST_LOAN_ID)).thenReturn(Optional.of(loan));

        Location location = Location.builder().id(TEST_LOCATION_ID).build();
        when(mockLocationDao.getBookLocation(TEST_BOOK_ID, false)).thenReturn(Optional.of(location));
        doNothing().when(mockService).updateUsersKarma(mockManager, loan);

        boolean result = mockService.returnBookCommand(mockManager, TEST_LOAN_ID);

        verify(mockLocationDao).updateIsOccupied(TEST_LOCATION_ID, true);
        verify(mockLoanDao).updateReturnDate(TEST_LOAN_ID, LocalDate.now());
        verify(mockService).updateUsersKarma(mockManager, loan);
        assertTrue(result);
    }

    @Test
    public void returnBookCommandShouldReturnFalseWithEmptyLoanOptional() throws SQLException {
        boolean result = mockService.returnBookCommand(mockManager, TEST_LOAN_ID);

        verify(mockLocationDao, never()).getBookLocation(anyLong(), anyBoolean());
        verify(mockLoanDao, never()).updateReturnDate(anyLong(), any(LocalDate.class));
        verify(mockService, never()).updateUsersKarma(eq(mockManager), any(Loan.class));
        assertFalse(result);
    }

    @Test
    public void returnBookCommandShouldReturnFalseWithEmptyLocationOptional() throws SQLException {
        Loan loan = Loan.builder()
                .bookId(TEST_BOOK_ID)
                .build();
        when(mockLoanDao.get(TEST_LOAN_ID)).thenReturn(Optional.of(loan));

        boolean result = mockService.returnBookCommand(mockManager, TEST_LOAN_ID);

        verify(mockLocationDao, never()).updateIsOccupied(anyLong(),anyBoolean());
        verify(mockLoanDao, never()).updateReturnDate(anyLong(), any(LocalDate.class));
        verify(mockService, never()).updateUsersKarma(eq(mockManager), any(Loan.class));
        assertFalse(result);
    }

    @Test
    public void returnBookCommandShouldReturnFalseWithExistentLoanReturnDate() throws SQLException {
        Loan loan = Loan.builder()
                .bookId(TEST_BOOK_ID)
                .returnDate(LocalDate.now())
                .build();
        when(mockLoanDao.get(TEST_LOAN_ID)).thenReturn(Optional.of(loan));

        boolean result = mockService.returnBookCommand(mockManager, TEST_LOAN_ID);

        verify(mockLocationDao, never()).getBookLocation(anyLong(), anyBoolean());
        verify(mockLoanDao, never()).updateReturnDate(anyLong(), any(LocalDate.class));
        verify(mockService, never()).updateUsersKarma(eq(mockManager), any(Loan.class));
        assertFalse(result);
    }

    @Test
    public void updateUsersKarmaShouldDecreaseUsersKarma() throws SQLException {
        Loan loan = Loan.builder()
                .userId(TEST_USER_ID)
                .expiredDate(LocalDate.now().minusDays(1))
                .build();

        mockService.updateUsersKarma(mockManager, loan);

        verify(mockUserDao, only()).updateKarma(TEST_USER_ID, -1);
    }

    @Test
    public void updateUsersKarmaShouldNotDecreaseUsersKarma() throws SQLException {
        Loan loan = Loan.builder()
                .userId(TEST_USER_ID)
                .expiredDate(LocalDate.now())
                .build();

        mockService.updateUsersKarma(mockManager, loan);

        verify(mockUserDao, never()).updateKarma(anyLong(), anyInt());
    }

    @Test
    public void addBookQuantityToLoanDtoInListShouldProperlyAddAuthors() throws SQLException {
        List<Author> authorList = new ArrayList<>();
        authorList.add(Author.builder().firstName("test").build());
        when(mockAuthorDao.getByBookId(TEST_BOOK_ID)).thenReturn(authorList);

        mockService.addAuthorsToLoanDtoInList(mockManager, loanDtoList);
        for (LoanDto dto : loanDtoList) {
            assertEquals(dto.getAuthors(), authorList);
        }
    }

    @Test
    public void addBookQuantityToLoanDtoInListShouldProperlyAddBookQuantity() throws SQLException {
        when(mockLocationDao.getBookQuantity(TEST_BOOK_ID)).thenReturn(42);

        mockService.addBookQuantityToLoanDtoInList(mockManager, loanDtoList);
        for (LoanDto dto : loanDtoList) {
            assertEquals(42, dto.getBookQuantity());
        }
    }
}