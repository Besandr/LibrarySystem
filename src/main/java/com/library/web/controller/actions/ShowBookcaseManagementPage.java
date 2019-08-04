package com.library.web.controller.actions;

import com.library.web.controller.ServletResources;
import com.library.web.controller.forms.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Action for showing bookcase management page
 */
public class ShowBookcaseManagementPage extends Action {

    /**
     * Forwards to the bookcase management page
     * @param request the request need to be processed
     * @param response the response to user
     * @param form - form need to be processed by this action
     * @param resources - servlet's resources
     * @return path to the bookcase management page
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, ActionForm form, ServletResources resources) {
        return resources.getForward("ShowBookcaseManagementPage");
    }
}
