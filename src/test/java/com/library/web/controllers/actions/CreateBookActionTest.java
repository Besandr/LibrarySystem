package com.library.web.controllers.actions;

import com.library.services.BookService;
import com.library.web.controllers.ServletResources;
import com.library.web.controllers.forms.BookCreationForm;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CreateBookActionTest {

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    ServletResources resources;

    @Mock
    BookService service;

    private CreateBookAction action;
    private BookCreationForm form;

    @Before
    public void init() {
        action = new CreateBookAction();
        action.setBookService(service);
        form = new BookCreationForm();
    }

    @Test
    public void executeShouldSetRequestAttribute() {
        when(service.addBookToCatalogue(isNull(), anyInt(), isNull()
                , isNull(), isNull(), isNull(), isNull(), isNull())).thenReturn(3L);
        action.execute(request, response, form, resources);
        verify(request).setAttribute("bookId", 3L);
    }

    @Test
    public void executeShouldProperlyPassFormArgumentsToService() {
        List<String> newAuthorsFirstNames = Collections.singletonList("testFirst");
        List<String> newAuthorsLastNames = Collections.singletonList("testLast");
        List<String> newKeywords = Collections.singletonList("testWord");
        List<Long> oldAuthorsId = Collections.singletonList(3L);
        List<Long> oldKeywordsId = Collections.singletonList(7L);

        form.setTitle("testTitle");
        form.setYear(1000);
        form.setDescription("testDescr");
        form.setNewAuthorFirstNames(newAuthorsFirstNames);
        form.setNewAuthorLastNames(newAuthorsLastNames);
        form.setOldAuthorsId(oldAuthorsId);
        form.setOldKeywordsId(oldKeywordsId);
        form.setNewKeywords(newKeywords);

        action.execute(request, response, form, resources);

        verify(service).addBookToCatalogue("testTitle", 1000, "testDescr", oldAuthorsId
                , oldKeywordsId, newAuthorsFirstNames, newAuthorsLastNames, newKeywords);
    }

    @Test
    public void executeShouldForwardToBookManagementAction() {
        when(resources.getForward("BookManagementAction")).thenReturn("testPath");
        String returnedPath = action.execute(request, response, form, resources);
        assertEquals("testPath", returnedPath);
    }

}