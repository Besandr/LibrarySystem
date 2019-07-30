package com.library.web.filters;

import com.library.repository.entity.Role;
import com.library.repository.entity.User;
import com.library.web.controller.ParameterRequestWrapper;
import com.library.web.controller.ServletResources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides authorization system for web-resources
 */
public class SecurityFilter implements Filter {

    private static final Logger log = LogManager.getLogger(SecurityFilter.class);

    /**
     * Checks is resource constrained. If it is, redirects not logged in users
     * to login page. If logged in user tries to request a constrained resource
     * checks user's rights for this request. If user hasn't enough rights for
     * this redirects him to access denied page.
     * Before redirecting the user to the login page saves the request path and
     * request parameters as session attributes. Them will be handled after
     * successful login by corresponding {@code Action}
     * @param req {@inheritDoc}
     * @param resp {@inheritDoc}
     * @param chain {@inheritDoc}
     * @throws ServletException if an exception has occurred that interferes with the
     *                         filterChain's normal operation
     * @throws IOException if an I/O related error has occurred during the processing
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {

        HttpSession session = ((HttpServletRequest) req).getSession();
        ServletResources resources = (ServletResources) req.getServletContext().getAttribute("servletResources");
        String resourcePath = ((HttpServletRequest) req).getServletPath();
        Map<String, List<Role>> securityConstraints = resources.getSecurityConstraints();
        log.debug("Request path" + ((HttpServletRequest) req).getServletPath());

        if (isResourceConstrained(resourcePath, securityConstraints)) {
            if (isUserLoggedIn(session)) {

                if (isUserAuthorized(session, resourcePath, securityConstraints)) {
                    allowPassing((HttpServletRequest) req, resp, chain);
                } else {
                    forwardAccessDenied((HttpServletRequest)req, resp, resources);
                }
            } else {
                forwardUserForLogin((HttpServletRequest) req, resp, resources);
            }
        } else {
            chain.doFilter(req, resp);
        }
    }

    /**
     * Directs an authorized user to the requested resource
     */
    private void allowPassing(HttpServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpSession session = req.getSession();
        Map<String, String[]> postponedRequestParameters = (Map<String, String[]>) session.getAttribute("postponedRequestParameters");

        if (postponedRequestParameters != null) {
            req = new ParameterRequestWrapper( req, postponedRequestParameters);
            removePostponedRequestParameters(session);
        }

        chain.doFilter(req, resp);
    }

    /**
     * Forwards an unauthorized user to access denied page
     */
    private void forwardAccessDenied(HttpServletRequest req, ServletResponse resp, ServletResources resources) throws ServletException, IOException {
        removePostponedRequestParameters(req.getSession());

        String forwardPath = resources.getForward("ShowAccessDeniedPage");
        req.getRequestDispatcher(forwardPath).forward(req, resp);
    }

    /**
     * Checks whether requested resource constrained
     * @param resourcePath - path to the requested resource
     * @param securityConstraints - mapping between constrained resources
     *                  and authorized roles
     * @return {@code true} if the requested resource need authorization
     * and {@code false} if it doesn't
     */
    private boolean isResourceConstrained(String resourcePath, Map<String, List<Role>> securityConstraints) {
        return securityConstraints.keySet().stream().anyMatch(resourcePath::startsWith);
    }

    /**
     * Checks whether current user logged in
     * @param session - session of current user
     * @return {@code true} if user logged in and {@code false} if it doesn't
     */
    private boolean isUserLoggedIn(HttpSession session) {
        return session.getAttribute("loggedInUser") != null;
    }

    /**
     * Checks whether current user authorized for accessing to {@code resourcePath}
     * @param session - session of current user
     * @param resourcePath - path to the requested resource
     * @param securityConstraints - mapping between constrained resources
     *                   and authorized roles
     * @return {@code true} if user authorized and {@code false} if it doesn't
     */
    private boolean isUserAuthorized(HttpSession session, String resourcePath, Map<String, List<Role>> securityConstraints) {
        Role userRole = ((User) session.getAttribute("loggedInUser")).getRole();
        List<Role> allowedRoles = securityConstraints.get(securityConstraints.keySet()
                .stream()
                .filter(resourcePath::startsWith)
                .findFirst()
                .get());
        return allowedRoles.contains(userRole);
    }

    /**
     * Saves the request path and request parameters as session attributes
     * and forwards user to login page
     */
    private void forwardUserForLogin(HttpServletRequest req, ServletResponse resp, ServletResources resources) throws ServletException, IOException {
        Map<String, String[]> parameterMap = new HashMap<>(req.getParameterMap());
        req.getSession().setAttribute("postponedRequestParameters", parameterMap);

        String postponedRequestUrl = req.getContextPath() + req.getServletPath();
        req.getSession().setAttribute("postponedRequestUrl", postponedRequestUrl);
        req.setAttribute("needAuthentication", "login.needAuthentication");

        String forwardPath = resources.getForward("ShowLoginPage");
        req.getRequestDispatcher(forwardPath).forward(req, resp);
    }

    /**
     * Removes stored in session path and parameters of
     * postponed request.
     */
    private void removePostponedRequestParameters(HttpSession session) {
        session.removeAttribute("postponedRequestParameters");
        session.removeAttribute("postponedRequestUrl");
    }
}
