package com.library.web.filters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Creates new session and stores request's servlet path as a session attribute
 */
public class SessionFilter implements Filter {

    private static final Logger log = LogManager.getLogger(SessionFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpSession session = ((HttpServletRequest) request).getSession();

        storeRequestPath(session, ((HttpServletRequest) request).getServletPath());

        chain.doFilter(request, response);
    }

    /**
     * Stores last two request's servlet paths like a two-slot stack.
     * If current request is the same as previous - doesn't save it
     * @param session - the user's session
     * @param servletPath - current servlet path
     */
    private void storeRequestPath(HttpSession session, String servletPath) {
        String previousPath = (String) session.getAttribute("previousRequestPath");
        if (!previousPath.equals(servletPath)) {
            session.setAttribute("prePreviousRequestPath", previousPath);
            session.setAttribute("previousRequestPath", servletPath);
            log.debug("Previous stored path: " + previousPath + ". Current servlet path: " + servletPath);
        }
    }

    @Override
    public void init(FilterConfig filterConfig){}

    @Override
    public void destroy() {}
}
