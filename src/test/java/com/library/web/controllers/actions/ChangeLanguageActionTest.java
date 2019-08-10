package com.library.web.controllers.actions;

import com.library.web.controllers.ServletResources;
import com.library.web.controllers.forms.ActionForm;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ChangeLanguageActionTest {

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    HttpSession session;

    @Mock
    ActionForm form;

    @Mock
    ServletResources resources;

    @Before
    public void mockInit(){
        when(request.getSession()).thenReturn(session);
    }

    @Test
    public void testExecute() {
        String expectedReturnValue = "Expected URL";

        when(request.getParameter("chosenLanguage")).thenReturn("RUSSIAN");

        ArgumentCaptor languageCodeCaptor = ArgumentCaptor.forClass(Object.class);
        doNothing().when(session).setAttribute(eq("language"), languageCodeCaptor.capture());

        ChangeLanguageAction action = spy(new ChangeLanguageAction());
        when(action.getRedirectToReferer(request, resources)).thenReturn(expectedReturnValue);

        String returnedValue = action.execute(request, response, form, resources);

        assertEquals("Method execute() should set correct language code",
                "ru", languageCodeCaptor.getValue());

        assertEquals("Method execute() should return expected value",
                expectedReturnValue, returnedValue);
    }

    @Test
    public void executeShouldNotChangeLanguage(){
        new ChangeLanguageAction().execute(request, response, form, resources);

        verify(session, never()).setAttribute(eq("language"), any());
    }
}