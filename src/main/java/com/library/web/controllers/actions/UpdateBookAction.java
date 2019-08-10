package com.library.web.controllers.actions;

import com.library.repository.dto.BookDto;
import com.library.services.BookService;
import com.library.services.Service;
import com.library.web.controllers.ServletResources;
import com.library.web.controllers.forms.ActionForm;
import com.library.web.controllers.forms.BookCreationForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Action for updating book information
 */
public class UpdateBookAction extends Action {

    private BookService bookService;

    /**
     * Stores updated book data by {@code BookService} and sets result
     * of this operation in a request
     * @param request the request need to be processed
     * @param response the response to user
     * @param form - form need to be processed by this action
     * @param resources - servlet's resources
     * @return path to the {@code ShowBookManagementAction}
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, ActionForm form, ServletResources resources) {
        BookDto bookDto = (BookDto) request.getSession().getAttribute("bookDto");

        BookCreationForm creationForm = (BookCreationForm) form;

        boolean updatingResult = bookService.updateBookDtoProperties(
                bookDto.getBook().getId(),
                creationForm.getTitle(),
                creationForm.getYear(),
                creationForm.getDescription(),
                creationForm.getOldAuthorsId(),
                creationForm.getOldKeywordsId(),
                creationForm.getNewAuthorFirstNames(),
                creationForm.getNewAuthorLastNames(),
                creationForm.getNewKeywords());

        setUpdatingResultInRequest(request, updatingResult, bookDto);

        return resources.getForward("BookManagementAction");

    }

    /**
     * Sets results of updating operation in a request
     * @param request for storing results
     * @param updatingResult - result need to be stored
     * @param bookDto - contains updated book information need to
     *                be stored
     */
    private void setUpdatingResultInRequest(HttpServletRequest request, boolean updatingResult, BookDto bookDto) {
        if (updatingResult) {
            request.setAttribute("actionResult", "bookManagement.updateBook.updatingSuccessful");
            request.setAttribute("bookId", bookDto.getBook().getId());
        } else {
            request.setAttribute("actionResult", "bookManagement.updateBook.updatingFailed");
        }
    }

    public void setBookService(Service bookService) {
        this.bookService = (BookService) bookService;
    }

}
