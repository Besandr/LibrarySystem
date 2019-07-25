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

}