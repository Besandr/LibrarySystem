package com.library.web.controllers.actions;

import com.library.services.LocationService;
import com.library.web.controllers.ServletResources;
import com.library.web.controllers.forms.AddBookcaseForm;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AddBookcaseActionTest {

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    ServletResources resources;

    @Mock
    LocationService locationService;

    private AddBookcaseAction action;
    private AddBookcaseForm form;

    @Before
    public void init() {
        action = new AddBookcaseAction();
        form = new AddBookcaseForm();
        action.setLocationService(locationService);
    }

    @Test
    public void executeShouldProperlySaveBookcase() {
        form.setShelfQuantity(5);
        form.setCellQuantity(10);
        action.execute(request, response, form, resources);
        verify(locationService).addBookcaseToStorage(5, 10);
    }

    @Test
    public void executeShouldSaveBookcaseAndSetSuccessfulAttribute() {
        when(locationService.addBookcaseToStorage(anyInt(), anyInt())).thenReturn(true);
        action.execute(request, response, form, resources);
        verify(request).setAttribute("actionResult", "bookcaseManagement.addingBookcase.successful");
    }

    @Test
    public void executeShouldSaveBookcaseAndSetFailedAttribute() {
        when(locationService.addBookcaseToStorage(anyInt(), anyInt())).thenReturn(false);
        action.execute(request, response, form, resources);
        verify(request).setAttribute("actionResult", "bookcaseManagement.addingBookcase.failed");
    }

    @Test
    public void executeShouldForwardToBookcaseManagementPage() {
        when(resources.getForward("ShowBookcaseManagementPage")).thenReturn("testPath");
        String returnedPath = action.execute(request, response, form, resources);
        assertEquals("testPath", returnedPath);
    }

}