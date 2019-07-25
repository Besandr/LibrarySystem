package com.library.web.controller.actions;

import com.library.web.controller.ActionServletConfigurator;
import com.library.web.controller.forms.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Action for forwarding request to user registration page
 */
public class ShowUserRegistrationPageAction extends Action {

    /**
     * Gives a path to user registration page
     * @param request the request need to be processed
     * @param response the response to user
     * @param form - form need to be processed by this action
     * @param servletConfigurator - configurations of servlet
     * @return a path to user registration page
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, ActionForm form, ActionServletConfigurator servletConfigurator) {
        return servletConfigurator.getForward("ShowRegistrationPage");
    }
}
