package com.library.web.controller.actions;

import com.library.web.controller.ServletResources;
import com.library.web.controller.forms.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Objects;

/**
 * Action for forwarding request to title page
 */
public class ShowLoginPageAction extends Action {

    /**
     * Gives a path to the login page
     * @param request the request need to be processed
     * @param response the response to user
     * @param form - form need to be processed by this action
     * @param resources - servlet's resources
     * @return path to the title page
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, ActionForm form, ServletResources resources) {
        saveReferentUrl(request, resources);
        return resources.getForward("ShowLoginPage");
    }

    /**
     * Saves the referent URL for possibility to come back there
     * after login
     */
    private void saveReferentUrl(HttpServletRequest request, ServletResources resources) {
        HttpSession session = request.getSession();
        //Is referent URL session attribute not exist
        if (session.getAttribute("referentUrl") == null) {
            //Setting the referent URL
            String referentUrl = (String) session.getAttribute("previousRequestPath");
            //Is pre-previous request path session attribute not exist
            if (referentUrl == null) {
                referentUrl = resources.getForward("ShowTitlePage");
            }
            session.setAttribute("referentUrl", referentUrl);
        }
    }
}