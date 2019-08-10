package com.library.web.controllers.actions;

import com.library.web.controllers.ServletResources;
import com.library.web.controllers.forms.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Action for logout user from system
 */
public class LogoutAction extends Action {

    /**
     * Logout user from system
     * @param request the request need to be processed
     * @param response the response to user
     * @param form - form need to be processed by this action
     * @param resources - servlet's resources
     * @return redirect path to the title page
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, ActionForm form, ServletResources resources) {
        request.getSession().removeAttribute("loggedInUser");
        return resources.getForward("ShowTitlePage");
    }
}
