package com.library.web.controllers.actions;

import com.library.services.LocationService;
import com.library.services.Service;
import com.library.web.controllers.ServletResources;
import com.library.web.controllers.forms.ActionForm;
import com.library.web.controllers.forms.AddBookcaseForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Action for adding bookcases to the library's storage
 */
public class AddBookcaseAction extends Action {

    private LocationService locationService;

    /**
     * Adds a new bookcase to the library's storage provides
     * new cells for storing books.
     * @param request the request need to be processed
     * @param response the response to user
     * @param form - form need to be processed by this action
     * @param resources - servlet's resources
     * @return a path to the bookcase management page
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, ActionForm form, ServletResources resources) {
        int shelfQuantity = ((AddBookcaseForm) form).getShelfQuantity();
        int cellQuantity = ((AddBookcaseForm) form).getCellQuantity();

        boolean addingResult = locationService.addBookcaseToStorage(shelfQuantity, cellQuantity);

        if (addingResult) {
            request.setAttribute("actionResult", "bookcaseManagement.addingBookcase.successful");
        } else {
            request.setAttribute("actionResult", "bookcaseManagement.addingBookcase.failed");
        }

        return resources.getForward("ShowBookcaseManagementPage");
    }

    public void setLocationService(Service locationService){
        this.locationService = (LocationService) locationService;
    }
}
