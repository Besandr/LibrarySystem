package com.library.web.controller;

/**
 * {@code RuntimeException} which may be thrown during
 * initialization {@code ActionServlet} configuration
 */
public class ServletConfigException extends RuntimeException {

    public ServletConfigException(String message) {
        super(message);
    }

    public ServletConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}
