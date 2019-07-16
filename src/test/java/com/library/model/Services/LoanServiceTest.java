package com.library.model.Services;

import com.library.model.data.DaoManager;
import com.library.model.data.dao.LoanDao;
import com.library.model.data.entity.Loan;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.SQLException;

import static org.junit.Assert.*;
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
    Loan mockLoan;

    @Before
    public void initSetUp() throws SQLException {
        mockService = spy(LoanService.getInstance());
        when(mockManager.getLoanDao()).thenReturn(mockLoanDao);
        when(mockLoanDao.save(mockLoan)).thenReturn(1L);
    }

    @Test
    public void saveApplyForLoanCommandShouldReturn1() throws SQLException {
        long id = mockService.saveApplyForLoanCommand(mockManager, mockLoan);
        verify(mockLoanDao, times(1)).save(mockLoan);
        assertEquals(1, id);
    }

}