package com.library.web.controllers.actions;

import com.library.repository.dto.BookDto;
import com.library.repository.entity.Book;
import com.library.services.BookService;
import com.library.web.controllers.PaginationHelper;
import com.library.web.controllers.ServletResources;
import com.library.web.controllers.forms.BookSearchForm;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BookSearchActionTest {

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    ServletResources resources;

    @Mock
    BookService service;

    @Mock
    PaginationHelper helper;

    private BookSearchAction action;
    private BookSearchForm form;
    private final long TEST_AUTHOR_ID = 3L;
    private final long TEST_KEYWORD_ID = 7L;
    private final String TEST_TITLE = "testTitle";


    @Before
    public void init() {
        action = spy(new BookSearchAction());
        action.setBookService(service);
        form = new BookSearchForm();
    }



    @Test
    public void executeShouldForwardToBookSearchPage() {
        when(resources.getForward("ShowBookSearchPage")).thenReturn("testPath");
        String returnedPath = action.execute(request, response, form, resources);
        assertEquals("testPath", returnedPath);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void executeShouldProperlyCallLocalMethods() {
        form.setAuthorId(TEST_AUTHOR_ID);
        form.setKeywordId(TEST_KEYWORD_ID);
        form.setBookTitle(TEST_TITLE);

        List<BookDto> list = Collections.singletonList(BookDto.builder()
                .book(Book.builder().id(17L).build())
                .build());

        doReturn(list).when(action).getBooksList(eq(request), eq(TEST_AUTHOR_ID)
                , eq(TEST_KEYWORD_ID), eq(TEST_TITLE), eq(service), any(PaginationHelper.class));
        doNothing().when(action).setRequestAttributes(eq(request), anyLong(), anyLong(), anyString(), any(List.class));
        doNothing().when(action).addPaginationToRequest(eq(request),eq(service), anyLong(), anyLong(), anyString(), any(PaginationHelper.class));

        action.execute(request, response, form, resources);

        verify(action).setRequestAttributes(request, TEST_AUTHOR_ID, TEST_KEYWORD_ID, TEST_TITLE, list);
        verify(action).addPaginationToRequest(eq(request), eq(service), eq(TEST_AUTHOR_ID), eq(TEST_KEYWORD_ID), eq(TEST_TITLE), any(PaginationHelper.class));
    }

    @Test
    public void setRequestAttributesShouldProperlySetAttributes() {
        action.setRequestAttributes(request, TEST_AUTHOR_ID, TEST_KEYWORD_ID, TEST_TITLE, Collections.emptyList());
        verify(request).setAttribute("authorId", TEST_AUTHOR_ID);
        verify(request).setAttribute("keywordId", TEST_KEYWORD_ID);
        verify(request).setAttribute("bookTitle", TEST_TITLE);
        verify(request).setAttribute("books", Collections.emptyList());
    }

    @Test
    public void getBooksListShouldProperlyGetList() {
        when(helper.getRecordsPerPage()).thenReturn(10);
        when(helper.getPreviousRecordNumber(request, 10)).thenReturn(20);

        action.getBooksList(request, TEST_AUTHOR_ID, TEST_KEYWORD_ID, TEST_TITLE, service, helper);
        verify(service).findBooks(TEST_AUTHOR_ID, TEST_KEYWORD_ID, TEST_TITLE, 10, 20);
    }

    @Test
    public void addPaginationToRequestShouldProperlyAddPagination() {
        when(service.getBookSearchResultCount(TEST_AUTHOR_ID, TEST_KEYWORD_ID, TEST_TITLE)).thenReturn(42L);
        action.addPaginationToRequest(request, service, TEST_AUTHOR_ID, TEST_KEYWORD_ID, TEST_TITLE, helper);
        verify(helper).addPaginationToRequest(request, 42L);
    }
}