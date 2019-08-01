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
     * Fills current form with data which given request contains
     * @param request - the request with form's data
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

    /**
     * Gives an converted to {@coed Long} ID from request
     * by id's parameter name
     * @param request with id property
     * @param idParameterName - name of request parameter with id
     * @return converted to {@coed Long} parameter with given name or
     * {@code 0} if there is no such parameter
     */
    long getIdPropertyFromRequest(HttpServletRequest request, String idParameterName) {
        try {
            return Long.parseLong(request.getParameter(idParameterName));
        } catch (NumberFormatException | NullPointerException e) {
            return 0;
        }
    }
}
