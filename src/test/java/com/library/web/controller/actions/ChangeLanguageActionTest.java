package com.library.web.controller.actions;

import com.library.web.controller.ServletResources;
import com.library.web.controller.forms.ActionForm;
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
        when(request.getParameter("currentPageUrl")).thenReturn(expectedReturnValue);

        ArgumentCaptor languageCodeCaptor = ArgumentCaptor.forClass(Object.class);
        doNothing().when(session).setAttribute(eq("language"), languageCodeCaptor.capture());

        ArgumentCaptor previousRequestPathCaptor = ArgumentCaptor.forClass(Object.class);
        doNothing().when(session).setAttribute(eq("previousRequestPath"), previousRequestPathCaptor.capture());

        String returnedValue = new ChangeLanguageAction().execute(request, response, form, resources);

        assertEquals("Method execute() should set correct language code",
                "ru", languageCodeCaptor.getValue());

        assertEquals("Method execute() should set correct previous request path attribute",
                expectedReturnValue, previousRequestPathCaptor.getValue());

        assertEquals("Method execute() should return expected value",
                expectedReturnValue, returnedValue);
    }

    @Test
    public void executeShouldNotChangeLanguage(){
        new ChangeLanguageAction().execute(request, response, form, resources);

        verify(session, never()).setAttribute(eq("language"), any());
    }
}