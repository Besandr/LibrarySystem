package com.library.web.controller;

import com.library.web.controller.actions.Action;
import com.library.web.controller.forms.ActionForm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

/**
 * Facade class for accessing {@code ActionServlet} resources.
 */
public class ServletResources {

    private static final Logger log = LogManager.getLogger(ServletResources.class);

    private final ActionFactory actionFactory;
    private final FormFactory formFactory;
    private final String REDIRECT_PREFIX = "redirect:";

    /**
     * Contains forwards names as {@code key} and resource paths as {@code value}
     */
    private final HashMap<String, String> forwards;

    ServletResources(ActionFactory actionFactory, FormFactory formFactory, HashMap<String, String> forwards) {
        this.actionFactory = actionFactory;
        this.formFactory = formFactory;
        this.forwards = forwards;
    }

    Action getAction(String actionPath) {
        return actionFactory.getAction(actionPath);
    }

    ActionForm getForm(String actionPath) {
        return formFactory.getForm(actionPath);
    }

    /**
     * Gives a {@code String} with path to resource bound with
     * given forward name.
     * @param forwardName - the forward's name
     * @return - path to target resource or path to "404" page
     * if there is no such forward name
     */
    public String getForward(String forwardName) {
        String forward = forwards.get(forwardName);
        if (forward == null) {
            log.error("Can't find forward by name: " + forwardName);
            return "/WEB-INF/jsp/404.jsp";
        } else {
            return forward;
        }
    }

    /**
     * Creates from given path special encoded path to
     * show that this path is should be redirected
     * @param path - the given plain path
     * @return - the path for redirection
     */
    public String createRedirectPath(String path) {
        return REDIRECT_PREFIX + path;
    }

    /**
     * Creates from given special encoded redirect path a
     * plain path (with ho special prefix)
     * @param pathWithPrefix the special encoded redirect path
     * @return - the plain decoded path
     */
    String getRedirectPath(String pathWithPrefix) {
        return pathWithPrefix.substring(REDIRECT_PREFIX.length());
    }

    /**
     * Checks whether given path is redirecting path
     * @param path - a path for checking
     * @return - a boolean result of path checking
     */
    boolean isRedirect(String path) {
        return path.startsWith(REDIRECT_PREFIX);
    }
}