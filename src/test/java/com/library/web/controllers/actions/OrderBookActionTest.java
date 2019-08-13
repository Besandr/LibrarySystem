package com.library.web.controllers.actions;

import com.library.repository.entity.User;
import com.library.services.LoanService;
import com.library.web.controllers.ServletResources;
import com.library.web.controllers.forms.OrderBookForm;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OrderBookActionTest {

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    ServletResources resources;

    @Mock
    LoanService loanService;

    @Mock
    HttpSession session;

    private OrderBookAction action;
    private OrderBookForm form;

    @Before
    public void init() {
        action = spy(new OrderBookAction());
        form = new OrderBookForm();
        action.setLoanService(loanService);
        when(request.getSession()).thenReturn(session);
    }

    @Test
    public void executeShouldMakeApplyForBookAndSetSuccessfulAttribute() {
        doReturn(false).when(action).makeApplyForBook(request, form);
        action.execute(request, response, form, resources);
        verify(request).setAttribute("orderResult", "bookSearch.result.failedOrder");
    }

    @Test
    public void executeShouldMakeApplyForBookAndSetFailedAttribute() {
        doReturn(true).when(action).makeApplyForBook(request, form);
        action.execute(request, response, form, resources);
        verify(request).setAttribute("orderResult", "bookSearch.result.successfulOrder");
    }

    @Test
    public void executeShouldForwardToBookSearchPage() {
        doReturn(false).when(action).makeApplyForBook(request, form);
        when(resources.getForward("ShowBookSearchPage")).thenReturn("testPath");
        String returnedPath = action.execute(request, response, form, resources);
        assertEquals("testPath", returnedPath);
    }

    @Test
    public void makeApplyForBookShouldProperlyPassArgumentsToLoanService() {
        form.setBookId(3L);
        User user = User.builder().id(11L).build();
        when(session.getAttribute("loggedInUser")).thenReturn(user);

        action.makeApplyForBook(request, form);
        verify(loanService).saveApplyForBook(3L, 11L);
    }

}