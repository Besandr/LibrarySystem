package com.library.web.filters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Creates new session if it wasn't created earlier
 */
public class SessionFilter implements Filter {

    private static final Logger log = LogManager.getLogger(SessionFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
       ((HttpServletRequest) request).getSession();

        chain.doFilter(request, response);
    }

}
