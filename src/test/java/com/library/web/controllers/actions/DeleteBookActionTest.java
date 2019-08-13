package com.library.web.controllers.actions;

import com.library.repository.dto.BookDto;
import com.library.repository.entity.Book;
import com.library.services.BookService;
import com.library.services.LocationService;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DeleteBookActionTest {

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    ActionForm form;

    @Mock
    ServletResources resources;

    @Mock
    HttpSession session;

    @Mock
    LocationService locationService;

    @Mock
    BookService bookService;

    private DeleteBookAction action;
    private BookDto bookDto;
    private final long TEST_BOOK_ID = 3L;

    @Before
    public void init() {
        action = new DeleteBookAction();
        action.setLocationService(locationService);
        action.setBookService(bookService);
        bookDto = BookDto.builder()
                .book(Book.builder().id(TEST_BOOK_ID).build())
                .build();
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("bookDto")).thenReturn(bookDto);
    }

    @Test
    public void executeShouldProperlyDeleteLoansWithBook() {
        action.execute(request, response, form, resources);
        verify(locationService).removeBookFromStorage(TEST_BOOK_ID);
    }

    @Test
    public void executeShouldProperlyRemoveBookFromCatalogue() {
        when(locationService.removeBookFromStorage(anyLong())).thenReturn(true);
        action.execute(request, response, form, resources);
        verify(bookService).removeBookFromCatalogue(bookDto);
    }

    @Test
    public void executeShouldSetRequestAttributeAndDoNotRemoveBook() {
        when(locationService.removeBookFromStorage(anyLong())).thenReturn(false);
        action.execute(request, response, form, resources);
        verify(bookService, never()).removeBookFromCatalogue(bookDto);
        verify(request).setAttribute("actionResult", "bookManagement.removeBook.removingFailed");
    }

    @Test
    public void executeShouldAddSuccessfulResultToRequest() {
        when(locationService.removeBookFromStorage(anyLong())).thenReturn(true);
        when(bookService.removeBookFromCatalogue(bookDto)).thenReturn(true);
        action.execute(request, response, form, resources);
        verify(request).setAttribute("actionResult", "bookManagement.deleteBook.deletingSuccessful");
    }

    @Test
    public void executeShouldAddFailedResultToRequest() {
        when(locationService.removeBookFromStorage(anyLong())).thenReturn(true);
        when(bookService.removeBookFromCatalogue(bookDto)).thenReturn(false);
        action.execute(request, response, form, resources);
        verify(request).setAttribute("actionResult", "bookManagement.deleteBook.deletingFailed");
    }

    @Test
    public void executeShouldForwardToBookManagementPage() {
        when(resources.getForward("ShowBookManagementPage")).thenReturn("testPath");
        String returnedPath = action.execute(request, response, form, resources);
        assertEquals("testPath", returnedPath);
    }

}