package com.library.web.controller.actions;

import com.library.repository.dto.BookDto;
import com.library.services.LocationService;
import com.library.services.Service;
import com.library.web.controller.ServletResources;
import com.library.web.controller.forms.ActionForm;
import com.library.web.controller.forms.BookManagementForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Action for removing books from the library's storage
 */
public class RemoveBookAction extends Action {

    LocationService locationService;

    /**
     * Removes all instances of target book from the library's storage
     * and stores in the request result of this operation
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

        if (removingResult) {
            request.setAttribute("actionResult", "bookManagement.removeBook.removingSuccessful");
        } else {
            request.setAttribute("actionResult", "bookManagement.removeBook.removingFailed");
        }

        return resources.getForward("ShowBookManagementPage");
    }

    public void setLocationService(Service locationService) {
        this.locationService = (LocationService) locationService;
    }
}
