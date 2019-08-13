package com.library.web.controllers.actions;

import com.library.repository.dto.BookDto;
import com.library.repository.entity.Book;
import com.library.services.LocationService;
import com.library.web.controllers.ServletResources;
import com.library.web.controllers.forms.BookManagementForm;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AddBooksActionTest {

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    HttpSession session;

    @Mock
    ServletResources resources;

    @Mock
    LocationService locationService;

    private AddBooksAction action;
    private BookManagementForm form;

    @Before
    public void init() {
        action = new AddBooksAction();
        form = new BookManagementForm();
        action.setLocationService(locationService);
        BookDto bookDto = BookDto.builder()
                .book(Book.builder().id(3L).build())
                .build();
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("bookDto")).thenReturn(bookDto);
    }

    @Test
    public void executeShouldProperlyAddBooks() {
        form.setBooksQuantity(31);
        action.execute(request, response, form, resources);
        verify(locationService).addBooksToStorage(3L, 31);
    }

    @Test
    public void executeShouldSaveBookAndSetSuccessfulAttribute() {
        when(locationService.addBooksToStorage(anyLong(), anyInt())).thenReturn(true);
        action.execute(request, response, form, resources);
        verify(request).setAttribute("actionResult", "bookManagement.addBook.addingSuccessful");
    }

    @Test
    public void executeShouldSaveBookAndSetFailedAttribute() {
        when(locationService.addBooksToStorage(anyLong(), anyInt())).thenReturn(false);
        action.execute(request, response, form, resources);
        verify(request).setAttribute("actionResult", "bookManagement.addBook.addingFailed");
    }

    @Test
    public void executeShouldForwardToBookManagementPage() {
        when(resources.getForward("ShowBookManagementPage")).thenReturn("testPath");
        String returnedPath = action.execute(request, response, form, resources);
        assertEquals("testPath", returnedPath);
    }
}