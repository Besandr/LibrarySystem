package com.library.web.controllers;

import com.library.repository.entity.Role;
import com.library.web.controllers.actions.Action;
import com.library.web.controllers.forms.ActionForm;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

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
    private final Map<String, String> forwards;

    /**
     * Contains URL-patterns of constrained resources and allowed for access
     * to them roles
     */
    @Getter
    private final Map<String, List<Role>> securityConstraints;

    public ServletResources(ActionFactory actionFactory, FormFactory formFactory, Map<String, String> forwards, Map<String, List<Role>> securityConstraints) {
        this.actionFactory = actionFactory;
        this.formFactory = formFactory;
        this.forwards = forwards;
        this.securityConstraints = securityConstraints;
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
