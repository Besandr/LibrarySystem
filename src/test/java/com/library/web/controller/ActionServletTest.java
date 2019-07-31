package com.library.web.controller;

import com.library.web.controller.actions.Action;
import com.library.web.controller.forms.ActionForm;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ActionServletTest {

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    ServletResources resources;

    @Mock
    Action action;

    @Mock
    ActionForm form;

    @Test
    public void getActionPathShouldReturnCorrectPath() {
        when(request.getServletPath()).thenReturn("testPath");
        assertEquals("testPath", new ActionServlet().getActionPath(request));
    }

    @Test
    public void getActionPathShouldReturnNull() {
        when(request.getServletPath()).thenReturn(null);
        assertNull(new ActionServlet().getActionPath(request));
    }

//    @Test
//    public void fillAndValidateFormShouldReturnEmptyErrors() {
//        ActionErrors errors = new ActionServlet().fillAndValidateForm(request, null, userAction);
//        assertFalse(errors.isHasErrors());
//    }

//    @Test
//    public void fillAndValidateFormShouldFillAndValidateForm() {
//        new ActionServlet().fillAndValidateForm(request, form, userAction);
//        verify(form, times(1)).fill(request);
//        verify(form, times(1)).validate();
//    }

//    @Test
//    public void executeActionShouldSetRequestAttributesAndReturnTestInputPath() {
//        ActionServlet servlet = spy(new ActionServlet());
//        ActionErrors errors = mock(ActionErrors.class);
//        String actionPath = "testPath";
//
//        when(action.isNeedValidate()).thenReturn(true);
//        when(resources.getForm(actionPath)).thenReturn(form);
//        doReturn(errors).when(servlet).fillAndValidateForm(request, form, userAction);
//        when(errors.isHasErrors()).thenReturn(true);
//        when(action.getInputPath()).thenReturn("testInputPath");
//
//        String executingResult = servlet.executeAction(request, response, action, actionPath, resources);
//
//        verify(request, times(1)).setAttribute("form", form);
//        verify(request, times(1)).setAttribute("errors", errors);
//        assertEquals("testInputPath", executingResult);
//    }

//    @Test
//    public void executeActionShouldValidateFormAndReturnForwardPath() {
//        ActionServlet servlet = spy(new ActionServlet());
//        ActionErrors errors = mock(ActionErrors.class);
//        String actionPath = "testPath";
//
//        when(action.isNeedValidate()).thenReturn(true);
//        when(resources.getForm(actionPath)).thenReturn(form);
//        doReturn(errors).when(servlet).fillAndValidateForm(request, form, userAction);
//        when(errors.isHasErrors()).thenReturn(false);
//        when(action.execute(request, response, form, resources)).thenReturn("testForwardPath");
//
//        String executingResult = servlet.executeAction(request, response, action, actionPath, resources);
//
//        assertEquals("testForwardPath", executingResult);
//    }

    @Test
    public void executeActionShouldNotValidateFormAndReturnForwardPath() {
        ActionServlet servlet = spy(new ActionServlet());
        ActionErrors errors = mock(ActionErrors.class);
        String actionPath = "testPath";

        when(action.isNeedValidate()).thenReturn(false);
        when(action.execute(request, response, null, resources)).thenReturn("testForwardPath");

        String executingResult = servlet.executeAction(request, response, action, actionPath, resources);

        assertEquals("testForwardPath", executingResult);
    }

    @Test
    public void processRequestShouldExecuteActionAndForward() throws ServletException, IOException {
        ActionServlet servlet = spy(new ActionServlet());
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);

        doReturn("testPath").when(servlet).getActionPath(request);
        when(resources.getAction("testPath")).thenReturn(action);
        when(servlet.executeAction(request, response, action,
                "path", resources)).thenReturn("forwardPath");
        when(request.getRequestDispatcher("forwardPath")).thenReturn(dispatcher);

        servlet.processRequest(request, response, resources);

        verify(dispatcher, times(1)).forward(request, response);
    }

    @Test
    public void processRequestShouldForwardTo404() throws ServletException, IOException {
        ActionServlet servlet = spy(new ActionServlet());
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);

        doReturn("testPath").when(servlet).getActionPath(request);
        when(resources.getAction("testPath")).thenReturn(null);
        when(request.getRequestDispatcher("WEB-INF/jsp/404.jsp")).thenReturn(dispatcher);

        servlet.processRequest(request, response, resources);

        verify(dispatcher, times(1)).forward(request, response);
    }
}