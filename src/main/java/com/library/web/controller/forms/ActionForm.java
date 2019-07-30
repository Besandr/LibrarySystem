package com.library.web.controller.forms;

import com.library.web.controller.ActionErrors;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;

/**
 * Base class for html form
 */
@NoArgsConstructor
public abstract class ActionForm {

    /**
     * Validates itself for errors
     * @return - object contains occurred errors
     */
    public abstract ActionErrors validate();

    /**
     * Fills current form with repository which given request contains
     * @param request - the request with form's repository
     */
    public abstract void fill(HttpServletRequest request);

    /**
     * Gives a string value for property from the given request
     * @param request - request with target parameter
     * @param propertyName - target property's name
     * @return - string value of target property or an
     * empty {@code String} if property wasn't found
     */
    String getPropertyFromRequest(HttpServletRequest request, String propertyName) {
        String parameter = request.getParameter(propertyName);
        return parameter == null ? "" : parameter;
    }
}
