package com.library.web.controllers.actions;

import com.library.web.controllers.ServletResources;
import com.library.web.controllers.forms.ActionForm;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ErrorLoggingActionTest {

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    ServletResources resources;

    @Mock
    Logger log;

    @Mock
    ActionForm form;

    private ErrorLoggingAction action;

    @Before
    public void init() {
        action = spy(new ErrorLoggingAction());
    }

    @Test
    public void executeShouldCallLogException() {
        Exception testException = new RuntimeException("Test exception message");
        when(request.getAttribute("javax.servlet.error.exception")).thenReturn(testException);
        doNothing().when(action).logException(any(Exception.class));
        action.execute(request, response, form, resources);
        verify(action).logException(testException);
    }

    @Test
    public void executeShouldNotCallLogExceptionWithoutExceptionAttributeInRequest() {
        action.execute(request, response, form, resources);
        verify(action, never()).logException(any(Exception.class));
    }

    @Test
    public void executeShouldReturnPathToErrorPage() {
        when(resources.getForward("showErrorPage")).thenReturn("testPath");
        String returnedPath = action.execute(request, response, form, resources);
        assertEquals("testPath", returnedPath);
    }

}