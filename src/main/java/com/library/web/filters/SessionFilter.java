package com.library.web.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Creates new session and stores each request's servlet path as a session attribute
 */
public class SessionFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpSession session = ((HttpServletRequest) request).getSession();

        session.setAttribute("previousRequest", ((HttpServletRequest) request).getServletPath());

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig){}

    @Override
    public void destroy() {}
}
