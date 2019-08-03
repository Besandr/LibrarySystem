package com.library.web.controller.actions;

import com.library.services.BookService;
import com.library.services.Service;
import com.library.web.controller.ServletResources;
import com.library.web.controller.forms.ActionForm;
import com.library.web.controller.forms.BookCreationForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Action for creating a new book in the library's catalogue
 */
public class CreateBookAction extends Action {

    private BookService bookService;

    /**
     * Passes the book's parameters from {@code form} to the
     * {@code BookService} for creating a new book in the library's catalogue.
     * If creating successful saves an ID of created book as a request parameter
     * and forwards to a book management page
     * @param request the request need to be processed
     * @param response the response to user
     * @param form - form need to be processed by this action
     * @param resources - servlet's resources
     * @return path for forwarding to a book management page
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, ActionForm form, ServletResources resources) {

        BookCreationForm creationForm = (BookCreationForm) form;

        long bookId = bookService.addBookToCatalogue(
                creationForm.getTitle(),
                creationForm.getYear(),
                creationForm.getDescription(),
                creationForm.getOldAuthorsId(),
                creationForm.getOldKeywordsId(),
                creationForm.getNewAuthorFirstNames(),
                creationForm.getNewAuthorLastNames(),
                creationForm.getNewKeywords());

        request.setAttribute("bookId", bookId);
        return resources.getForward("BookManagementAction");
    }

    public void setBookService(Service bookService) {
        this.bookService = (BookService) bookService;
    }

}
