package com.library.web.controllers.actions;

import com.library.repository.dto.BookDto;
import com.library.services.LocationService;
import com.library.services.Service;
import com.library.web.controllers.ServletResources;
import com.library.web.controllers.forms.ActionForm;
import com.library.web.controllers.forms.BookManagementForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Action for adding books to the library's storage
 */
public class AddBooksAction extends Action {

    private LocationService locationService;

    /**
     * Adds to library's storage needed quantity of {@code BookDto}
     * @param request the request need to be processed
     * @param response the response to user
     * @param form - form need to be processed by this action
     * @param resources - servlet's resources
     * @return path to book management page
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, ActionForm form, ServletResources resources) {
        BookDto bookDto = (BookDto) request.getSession().getAttribute("bookDto");
        int bookQuantity = ((BookManagementForm)form).getBooksQuantity();

        boolean addingSuccessful = locationService.addBooksToStorage(bookDto.getBook().getId(), bookQuantity);
        if (addingSuccessful) {
            request.setAttribute("actionResult", "bookManagement.addBook.addingSuccessful");
        } else {
            request.setAttribute("actionResult", "bookManagement.addBook.addingFailed");
        }

        request.setAttribute("bookDto", bookDto);
        return resources.getForward("ShowBookManagementPage");
    }

    public void setLocationService(Service locationService) {
        this.locationService = (LocationService) locationService;
    }

}
