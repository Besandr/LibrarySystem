package com.library.web.controllers.actions;

import com.library.services.LoanService;
import com.library.web.controllers.ServletResources;
import com.library.web.controllers.forms.ApproveLoanForm;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ApproveLoanActionTest {

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    ServletResources resources;

    @Mock
    LoanService loanService;

    private ApproveLoanAction action;
    private ApproveLoanForm form;

    @Before
    public void init() {
        action = new ApproveLoanAction();
        form = new ApproveLoanForm();
        action.setLoanService(loanService);
    }

    @Test
    public void executeShouldProperlyApproveLoan() {
        form.setLoanId(3L);
        action.execute(request, response, form, resources);
        verify(loanService).approveLoan(3L);
    }

    @Test
    public void executeShouldAddSessionAttributes() {
        final boolean TEST_BOOLEAN = true;
        form.setLoanId(3L);
        when(loanService.approveLoan(anyLong())).thenReturn(TEST_BOOLEAN);
        action.execute(request, response, form, resources);
        verify(request).setAttribute("approvingResult", TEST_BOOLEAN);
        verify(request).setAttribute("loanId", 3L);
    }

    @Test
    public void executeShouldForwardToShowingUnapprovedLoansPage() {
        when(resources.getForward("ShowUnapprovedLoansAction")).thenReturn("testPath");
        String returnedPath = action.execute(request, response, form, resources);
        assertEquals("testPath", returnedPath);
    }

}