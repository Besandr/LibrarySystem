package com.library.web.controllers.actions;

import com.library.web.controllers.ServletResources;
import com.library.web.controllers.forms.ActionForm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Action for catching and logging any exceptions unhandled before
 */
public class ErrorLoggingAction extends Action {

    private static final Logger log = LogManager.getLogger(ErrorLoggingAction.class);

    /**
     * Catches and logs any exceptions unhandled before.
     * Catches without logging any servers errors.
     * @param request the request need to be processed
     * @param response the response to user
     * @param form - form need to be processed by this action
     * @param resources - servlet's resources
     * @return path to the error page
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, ActionForm form, ServletResources resources) {

        Exception e = (Exception) request.getAttribute("javax.servlet.error.exception");
        if (e != null) {
            logException(e);
        }

        return resources.getForward("showErrorPage");
    }

    /**
     * Logs given exception
     * @param e - exception for logging
     */
    void logException(Exception e) {
        ErrorLoggingAction.log.error("Exception intercepted: " + e.getMessage(), e);
    }
}
