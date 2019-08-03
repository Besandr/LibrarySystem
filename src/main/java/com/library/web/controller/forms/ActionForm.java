package com.library.web.controller.forms;

import com.library.web.controller.ActionErrors;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
        return parameter == null ? "" : parameter.trim();
    }

    /**
     * Gives a {@code List} of properties for given
     * property name from the given request
     * @param request - request with target parameter
     * @param propertyName - target property's name
     * @return {@code List} with string values of target property or an
     * empty {@code List} if property wasn't found
     */
    List<String> getPropertyListFromRequest(HttpServletRequest request, String propertyName) {
        String[] parameters = request.getParameterValues(propertyName);
        if (parameters != null) {
            return Arrays.stream(parameters).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Gives an converted to {@coed Long} ID from request
     * by id's parameter name. It seeks property firstly in request's
     * parameters and then in request's attributes
     * @param request with id property
     * @param idParameterName - name of request parameter with id
     * @return converted to {@coed Long} parameter with given name or
     * {@code 0} if there is no such parameter or attribute
     */
    long getIdPropertyFromRequest(HttpServletRequest request, String idParameterName) {
        String idString = request.getParameter(idParameterName);
        if (idString != null && !idString.isEmpty()) {

            return Long.parseLong(idString);

        } else {

            Object idObj = request.getAttribute(idParameterName);
            if (idObj instanceof Long) {
                return (long) idObj;
            } else {
                return 0L;
            }
        }
    }
}
