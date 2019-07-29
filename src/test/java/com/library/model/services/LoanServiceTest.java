package com.library.model.services;

import com.library.model.data.DaoManager;
import com.library.model.data.DaoManagerFactory;
import com.library.model.data.dao.LoanDao;
import com.library.model.data.entity.Loan;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.SQLException;

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
        mockService = spy(new LoanService(new DaoManagerFactory()));
        when(mockManager.getLoanDao()).thenReturn(mockLoanDao);
    }

//    @Test
//    public void saveApplyForLoanCommand() throws SQLException {
//        mockService.saveApplyForLoanCommand(mockManager, mockLoan);
//        verify(mockLoanDao, times(1)).save(mockLoan);
//    }

}