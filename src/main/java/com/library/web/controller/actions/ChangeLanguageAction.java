package com.library.web.controller.actions;

import com.library.repository.entity.Languages;
import com.library.web.controller.ServletResources;
import com.library.web.controller.forms.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Action for changing web pages language
 */
public class ChangeLanguageAction extends Action{

    /**
     * Changes session language attribute to the another one
     * given in the request
     * @param request the request with a another language parameter
     * @param response the response to user
     * @param form this action not need form
     * @param resources - servlet's resources
     * @return path to page from what the language has been changed
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response,
                          ActionForm form, ServletResources resources) {

        String chosenLanguage = request.getParameter("chosenLanguage");

        //changing language if chosenLanguage parameter present
        if (chosenLanguage != null) {
            request.getSession().setAttribute("language", Languages.valueOf(chosenLanguage).getCode());
        }

        return getRedirectToReferer(request, resources);
    }
}
