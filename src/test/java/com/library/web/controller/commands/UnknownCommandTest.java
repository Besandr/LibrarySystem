package com.library.web.controller.commands;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UnknownCommandTest {

    @Test
    public void processShouldCallForwardWithStringError() throws Exception{

        FrontCommand spyCommand = spy(new UnknownCommand());
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);

        ArgumentCaptor valueCapture = ArgumentCaptor.forClass(String.class);

        doNothing().when(spyCommand).forward((String)valueCapture.capture());
        when(mockRequest.getParameter(RequestParameterNamesDict.COMMAND)).thenReturn("unknown");

        spyCommand.init(null, mockRequest, null);
        spyCommand.process();

        assertEquals("error", valueCapture.getValue());
    }

}