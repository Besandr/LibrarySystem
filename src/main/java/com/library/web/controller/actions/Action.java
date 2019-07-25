package com.library.web.controller.actions;

import com.library.web.controller.ActionServletConfigurator;
import com.library.web.controller.forms.ActionForm;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Base class for servlet's action
 */
@Getter
@Setter
public abstract class Action {

    /**
     * Path from what this action was executed
     */
    String inputPath;

    /**
     * Tells does this action requires a form with pre-validation
     */
    boolean needValidate;

    /**
     * Abstract method represents action itself
     * @param request the request need to be processed
     * @param response the response to user
     * @param form - form need to be processed by this action
     * @param servletConfigurator - configurations of servlet
     * @return - path where servlet should redirect request
     */
    public abstract String execute(HttpServletRequest request, HttpServletResponse response,
                                   ActionForm form, ActionServletConfigurator servletConfigurator);
}
