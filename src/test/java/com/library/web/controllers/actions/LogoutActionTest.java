package com.library.web.controllers.actions;

import com.library.web.controllers.ServletResources;
import com.library.web.controllers.forms.ActionForm;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LogoutActionTest {

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    ServletResources resources;

    @Mock
    ActionForm form;

    @Mock
    HttpSession session;

    private LogoutAction action;

    @Before
    public void init() {
        action = new LogoutAction();
        when(request.getSession()).thenReturn(session);
    }

    @Test
    public void executeShouldRemoveUserAttribute() {
        action.execute(request, response, form, resources);
        verify(session).removeAttribute("loggedInUser");
    }

    @Test
    public void executeShouldForwardToTitlePage() {
        when(resources.getForward("ShowTitlePage")).thenReturn("testPath");
        String returnedPath = action.execute(request, response, form, resources);
        assertEquals("testPath", returnedPath);
    }

}