package com.library.web.controllers.actions;

import com.library.repository.dto.BookDto;
import com.library.services.BookService;
import com.library.services.LocationService;
import com.library.services.Service;
import com.library.web.controllers.ServletResources;
import com.library.web.controllers.forms.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Action for deleting book from the library's catalogue
 */
public class DeleteBookAction extends Action {

    private LocationService locationService;
    private BookService bookService;

    /**
     * Firstly tries to remove a book from the library's storage.
     * If removing is successful tries to delete the book from
     * the library's catalogue.
     * @param request the request need to be processed
     * @param response the response to user
     * @param form - form need to be processed by this action
     * @param resources - servlet's resources
     * @return a path to the book management page
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, ActionForm form, ServletResources resources) {
        BookDto bookDto = (BookDto) request.getSession().getAttribute("bookDto");

        boolean removingResult = locationService.removeBookFromStorage(bookDto.getBook().getId());

        if (!removingResult) {
            request.setAttribute("actionResult", "bookManagement.removeBook.removingFailed");
        } else {
            boolean deletingResult = bookService.removeBookFromCatalogue(bookDto);
            if (deletingResult) {
                request.setAttribute("actionResult", "bookManagement.deleteBook.deletingSuccessful");
            } else {
                request.setAttribute("actionResult", "bookManagement.deleteBook.deletingFailed");
            }
        }

        return resources.getForward("ShowBookManagementPage");
    }

    public void setLocationService(Service locationService) {
        this.locationService = (LocationService) locationService;
    }

    public void setBookService(Service bookService) {
        this.bookService = (BookService) bookService;
    }


}
