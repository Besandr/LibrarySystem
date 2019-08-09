package com.library.web.filters;

import javax.servlet.*;
import java.io.IOException;

/**
 * Encodes requests and responses with UTF-8 by default
 */
public class EncodingFilter implements Filter {

    private String encoding;

    @Override
    public void init(FilterConfig config) throws ServletException {
        // read encoding param from config in web.xml
        encoding = config.getInitParameter("encoding");

        // if there is no such config set UTF-8
        if (encoding == null) {
            encoding = "UTF-8";
        }
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {

        request.setCharacterEncoding(encoding);
        response.setCharacterEncoding(encoding);
        chain.doFilter(request, response);
    }
}
